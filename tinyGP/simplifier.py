import sympy
import os
import threading


class Simplifier:

    path_to = ""
    path_from = ""

    def simplify(self, fileName: str):
        try:
            with open(self.path_from+fileName, "rb") as f:
                try:  # catch OSError in case of a one line file
                    f.seek(-2, os.SEEK_END)
                    while f.read(1) != b'\n':
                        f.seek(-2, os.SEEK_CUR)
                except OSError:
                    f.seek(0)
                last_line = f.readline().decode()
                last_line = last_line[1:]

            print("Simplifying from file: "+self.path_from+fileName)

            simplified = sympy.simplify(last_line).__str__()

            simplified = "="+simplified.replace("**", "^")

            with open(self.path_to+fileName, "w") as f:
                f.write(simplified)

            print(f"Simplified from {len(last_line)} chars to {len(simplified)} chars")
        except Exception:
            print("Couldn't simplify from file "+self.path_from+fileName)

    def run(self, path_from: str, path_to: str, from_dir: bool):
        self.path_from = path_from
        self.path_to = path_to

        if from_dir is False:
            self.simplify(path_from)
        else:
            files = os.listdir(path_from)
            for fileName in files:
                x = threading.Thread(target=self.simplify, args=(fileName,))
                x.start()
