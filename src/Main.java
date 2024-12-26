import linearEquation.GaussianEliminationSolver;
import linearEquation.LUDecompositionSolver;

public class Main {
    public static void main(String[] args) {
        System.out.println("====================Приклад 1====================");
        GaussianEliminationSolver gauss = new GaussianEliminationSolver();
        gauss.solve(new double[][] {
            {4, 2, -1},
            {1, 2, 1},
            {0, 1, -1}
        },
        new double[] {0, 1, -3});

        System.out.println();
        LUDecompositionSolver LU = new LUDecompositionSolver();
        LU.solve(new double[][] {
            {4, 2, -1},
            {1, 2, 1},
            {0, 1, -1}
        },
        new double[] {0, 1, -3});
        System.out.println();

        System.out.println("====================Приклад 2====================");

        gauss.solve(new double[][] {
            {-2, 1, 0},
            {1, -2, -1},
            {3, 4, -2}
        },
        new double[] {-6, 5, 13});
        System.out.println();

        LU.solve(new double[][] {
            {-2, 1, 0},
            {1, -2, -1},
            {3, 4, -2}
        },
        new double[] {-6, 5, 13});
        System.out.println();
        System.out.println("====================Приклад 3(невизначена система)====================");

        gauss.solve(new double[][] {
                        {3, 1, 4},
                        {2, 2, 3},
                        {1, -1, 1}
                },
                new double[] {1, 9, -8});
        System.out.println();

        LU.solve(new double[][] {
                        {3, 1, 4},
                        {2, 2, 3},
                        {1, -1, 1}
                },
                new double[] {1, 9, -8});
    }
}