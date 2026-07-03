import csv
import sys
from decimal import Decimal

import numpy_financial as npf

TOLERANCE = Decimal("0.001")


def expected_level_payment(balance: Decimal, rate: Decimal, n: int):
    r = float(rate)
    b = float(balance)
    ppmt = -npf.ppmt(r, 1, n, b)
    ipmt = -npf.ipmt(r, 1, n, b)
    return Decimal(str(ppmt)), Decimal(str(ipmt))


def expected_equal_principal(balance: Decimal, rate: Decimal, n: int, p0: Decimal, total: int):
    if n == 1:
        principal = balance
    else:
        principal = p0 / Decimal(total)
    interest = balance * rate
    return principal, interest


def expected_bullet(balance: Decimal, rate: Decimal, n: int):
    principal = balance if n == 1 else Decimal("0")
    interest = balance * rate
    return principal, interest


def close_enough(actual: Decimal, expected: Decimal) -> bool:
    return abs(actual - expected) <= TOLERANCE


def main(csv_path: str):
    total = 0
    failed = []
    max_diff = Decimal("0")

    with open(csv_path, newline="") as f:
        reader = csv.DictReader(f)
        for row in reader:
            total += 1
            method = row["method"]
            balance = Decimal(row["balance"])
            rate = Decimal(row["rate"])
            n = int(row["n"])
            p0 = Decimal(row["originalPrincipal"])
            total_installments = int(row["totalInstallments"])
            actual_principal = Decimal(row["principal"])
            actual_interest = Decimal(row["interest"])

            if method == "LEVEL_PAYMENT":
                exp_principal, exp_interest = expected_level_payment(balance, rate, n)
            elif method == "EQUAL_PRINCIPAL":
                exp_principal, exp_interest = expected_equal_principal(balance, rate, n, p0, total_installments)
            elif method == "BULLET":
                exp_principal, exp_interest = expected_bullet(balance, rate, n)
            else:
                raise ValueError(f"unknown method: {method}")

            principal_diff = abs(actual_principal - exp_principal)
            interest_diff = abs(actual_interest - exp_interest)
            max_diff = max(max_diff, principal_diff, interest_diff)

            if not close_enough(actual_principal, exp_principal) or not close_enough(actual_interest, exp_interest):
                failed.append({
                    "row": row,
                    "expected_principal": exp_principal,
                    "expected_interest": exp_interest,
                    "principal_diff": principal_diff,
                    "interest_diff": interest_diff,
                })

    print(f"total cases: {total}")
    print(f"passed:      {total - len(failed)}")
    print(f"failed:      {len(failed)}")
    print(f"max diff:    {max_diff}")

    if failed:
        print("\n--- failures (up to 20 shown) ---")
        for item in failed[:20]:
            row = item["row"]
            print(
                f"{row['method']} balance={row['balance']} rate={row['rate']} n={row['n']} "
                f"P0={row['originalPrincipal']} N={row['totalInstallments']} | "
                f"java principal={row['principal']} expected={item['expected_principal']} diff={item['principal_diff']} | "
                f"java interest={row['interest']} expected={item['expected_interest']} diff={item['interest_diff']}"
            )
        sys.exit(1)


if __name__ == "__main__":
    csv_path = sys.argv[1] if len(sys.argv) > 1 else "cases.csv"
    main(csv_path)
