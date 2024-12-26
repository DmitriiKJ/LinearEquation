package linearEquation;

import java.util.function.BiFunction;

public class GaussianEliminationSolver implements Solver{

    // Анонімний об'єкт
    PrintArray printLikeAnswerForLinearEquation = new PrintArray() {
        @Override
        public void print(double[] arr) {
            for (int i = 0; i < arr.length; i++){
                System.out.printf("x%d = %.2f ", i + 1, arr[i]);
            }
        }
    };

    @Description(description = "Використовується метод Гауса для розв'язання системи лінійних рівнянь")
    @Override
    public void solve(double[][] coefficients, double[] constants) {
        if (coefficients[0].length != constants.length || coefficients.length != coefficients[0].length) {
            throw new IllegalArgumentException("Невірні дані");
        }
        double[][] matr = new double[coefficients.length][coefficients[0].length + 1];

        // створюємо одну матрицю з коефіціентами та константами
        for (int i = 0; i < matr.length; i++) {
            for (int j = 0; j < matr[0].length; j++) {
                matr[i][j] = j < matr[0].length - 1 ? coefficients[i][j] : constants[i];
            }
        }

        // Приводимо до ступінчастого вигляду
        for (int i = 0; i < matr.length - 1; i++) {
            // Знаходимо ведучий елемент
            int lead = i;
            while (lead < matr[i].length && matr[i][lead] == 0) {
                lead++;
            }

            if (lead == matr[i].length) {
                continue; // Усі елементи в цьому рядку дорівнюють 0
            }

            double leadValue = matr[i][lead];
            for (int j = lead; j < matr[i].length; j++) {
                matr[i][j] /= leadValue;
            }

            // Лямбда-вираз
            BiFunction<Double, Double, Double> multValue = (x, y) -> x * y;

            for (int k = i + 1; k < matr.length; k++) {
                double factor = matr[k][lead];
                for (int j = lead; j < matr[i].length; j++) {
                    matr[k][j] -= multValue.apply(factor, matr[i][j]);
                }
            }
        }

        int rank = 0, rankExpanded = 0;
        for (int i = 0; i < matr.length; i++) {
            boolean forRank = false;
            boolean forRankExpanded = false;

            for (int j = 0; j < matr[0].length; j++) {
                // Округлення до 5 символів після коми
                if (Math.round(matr[i][j] * 100000.0) * 100000.0 != 0) {
                    if (j < matr[0].length - 1) {
                        forRank = true;
                        forRankExpanded = true;
                    } else { // у випадку якщо лише у матриці розширення є ненульовий елемент
                        forRankExpanded = true;
                    }
                    break;
                }
            }

            if (forRank) rank++;
            if (forRankExpanded) rankExpanded++;
        }

        // Якщо ранги матриці коефіціентів та розширеної матриці не рівні, то рівняння не має розв'язків
        if (rank != rankExpanded) {
            System.out.println("Система рівнянь не має розв'язків");
            return;
        }

        // Якщо ж ранг менше ніж кількість невідомих, то система невизначена (тобто має нескінчену кількість розв'язків)
        if (rank < matr[0].length - 1){
            System.out.println("Ця система не має конкретного розв'язку, вона не є визначеною (тобто має нескінчену кількість розв'язків). Ось один з можливих розв'язків: ");
        }

        // Якщо ранг матриці дорівнює кількості невідомих, то існує одне рішення
            double[] answer = new double[coefficients[0].length];
            int answIdx = matr[0].length - 1;
            for (int i = answer.length - 1; i >= 0; i--){
                double answOne = matr[i][answIdx];
                for (int j = answer.length - 1; j >= i ; j--){
                    if(j == i){
                        answOne /= matr[i][j];
                    }
                    else{
                        answOne -= matr[i][j] * answer[j];
                    }
                }
                answer[i] = answOne;
            }
            System.out.println("Рішення системи методом Гауса: ");
            printLikeAnswerForLinearEquation.print(answer);

    }
}
