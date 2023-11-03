import sympy
import os
import threading


class Simplifier:

    @staticmethod
    def simplify(fileName: str):
        try:
            with open(fileName, "rb") as f:
                try:  # catch OSError in case of a one line file
                    f.seek(-2, os.SEEK_END)
                    while f.read(1) != b'\n':
                        f.seek(-2, os.SEEK_CUR)
                except OSError:
                    f.seek(0)
                last_line = f.readline().decode()
                last_line = last_line[1:]

            print("Simplifying from file: "+fileName)

            simplified = sympy.simplify(last_line).__str__()

            simplified = "="+simplified.replace("**", "^")

            with open(fileName, "w") as f:
                f.write(simplified)

            print(f"Simplified from {len(last_line)} chars to {len(simplified)} chars")
        except Exception:
            print("Couldn't simplify from file "+fileName)

    def run(self, path: str, from_dir: bool):
        if from_dir is False:
            self.simplify(path)
        else:
            files = os.listdir(path)
            for fileName in files:
                x = threading.Thread(target=self.simplify, args=(path+fileName,))
                x.start()
