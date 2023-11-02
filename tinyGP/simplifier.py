import sympy
import os


class Simplifier:

    def simplify(self, fileName: str):
        with open("../tinyGP/gp_output/"+fileName, "rb") as f:
            try:  # catch OSError in case of a one line file
                f.seek(-2, os.SEEK_END)
                while f.read(1) != b'\n':
                    f.seek(-2, os.SEEK_CUR)
            except OSError:
                f.seek(0)
            last_line = f.readline().decode()
            last_line = last_line[1:]

        print("Simplifying from file: ../tinyGP/gp_output/"+fileName)

        simplified = sympy.simplify(last_line).__str__()

        simplified = "="+simplified.replace("**", "^")

        with open("../tinyGP/simplified_output/"+fileName, "w") as f:
            f.write(simplified)

        print(f"Simplified from {len(last_line)} chars to {len(simplified)} chars")