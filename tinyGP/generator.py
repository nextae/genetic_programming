import itertools
import numpy as np


def f1(x: float) -> float:
    return 5 * x ** 3 - 2 * x ** 2 + 3 * x - 17


def f2(x: float) -> float:
    return np.sin(x) + np.cos(x)


def f3(x: float) -> float:
    return 2 * np.log(x + 1)


def f4(x: float, y: float) -> float:
    return x + 2 * y


def f5(x: float, y: float) -> float:
    return np.sin(x / 2) + 2 * np.cos(x)


def f6(x: float, y: float) -> float:
    return x ** 2 + 3 * x * y - 7 * y + 1


FUNCTIONS = [f1, f2, f3, f4, f5, f6]

RANGES = {
    f1: [(-10, 10), (0, 100), (-1, 1), (-1000, 1000)],
    f2: [(-3.14, 3.14), (0, 7), (0, 100), (-100, 100)],
    f3: [(0, 4), (0, 9), (0, 99), (0, 999)],
    f4: [(-10, 10), (0, 100), (-1000, 1000)],
    f5: [(0, 1), (-10, 10), (0, 100), (-1000, 1000)],
    f6: [(-10, 10), (0, 100), (-1, 1), (-1000, 1000)],
}

VAR_COUNT = {
    f1: 1,
    f2: 1,
    f3: 1,
    f4: 2,
    f5: 2,
    f6: 2
}


def generate(n_rand: int, min_rand: int, max_rand: int, n_fit_cases: int):
    for i, function in enumerate(FUNCTIONS, 1):
        n_var = VAR_COUNT[function]
        ranges = RANGES[function]

        count = n_fit_cases / (2 ** (n_var - 1))
        if not count.is_integer():
            raise ValueError(f'Cannot equally divide the space for {n_fit_cases} fit cases and {n_var} variables')

        count = int(count) + 1

        header = f'{n_var} {n_rand} {min_rand} {max_rand} {count ** n_var}'

        for lower_bound, upper_bound in ranges:
            inputs = [np.linspace(lower_bound, upper_bound, count)] * n_var

            input_tuples = itertools.product(*inputs)
            outputs = ['\t'.join(str(value) for value in t) + f'\t{function(*t)}' for t in input_tuples]

            data = '\n'.join([header] + outputs)

            with open(f'data/function{i}_{lower_bound}_{upper_bound}.txt', 'w') as f:
                f.write(data)


generate(100, -5, 5, 100)
