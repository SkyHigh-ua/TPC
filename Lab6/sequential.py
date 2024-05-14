import numpy as np
import sys

default_args = [62, 15, 7]
args = sys.argv[1:]
args.extend(default_args[len(args):])
NRA = int(args[0])
NCA = int(args[1])
NCB = int(args[2])
a = np.full((NRA, NCA), 1.0)
b = np.full((NCA, NCB), 1.0)
np.dot(a, b)