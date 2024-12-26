package linearEquation;

import java.util.function.BiFunction;

public class LUDecompositionSolver implements Solver{

    // Анонімний об'єкт
    PrintArray printLikeAnswerForLinearEquation = new PrintArray() {
        @Override
        public void print(double[] arr) {
            for (int i = 0; i < arr.length; i++){
                System.out.printf("x%d = %.2f ", i + 1, arr[i]);
            }
        }
    };

    @Description(description = "Використовується LU розклад для розв'язання системи лінійних рівнянь")
    @Override
    public void solve(double[][] coefficients, double[] constants) {
        if(coefficients[0].length != constants.length || coefficients.length != coefficients[0].length){
            throw new IllegalArgumentException("Невірні дані");
        }

        if(!hasOneSolution(coefficients, constants)){
            return;
        }

        double[][] matrU = new double[coefficients.length][coefficients[0].length];
        double[][] matrL = new double[coefficients.length][coefficients[0].length];

        for (int i = 0; i < coefficients.length; i++){
            for (int j = 0; j < coefficients[0].length; j++){
                matrU[i][j] = 0;
                matrL[i][j] = 0;
            }
            matrL[i][i] = 1;
        }

        for (int i = 0; i < coefficients.length; i++){
            for (int j = 0; j < coefficients[0].length; j++){
                double tmpSum = 0;
                for (int k = 0; k <= i; k++){
                    tmpSum += matrU[k][j] * matrL[i][k];
                }

                if(i <= j){
                    matrU[i][j] = coefficients[i][j] - tmpSum;
                }
                else{
                    matrL[i][j] = (coefficients[i][j] - tmpSum)/matrU[j][j];
                }
            }
        }

        double[] tmpY = new double[matrL[0].length];
        for (int i = 0; i < matrL.length; i++){
            double answOne = constants[i];
            for (int j = 0; j <= i; j++){
                if(j == i){
                    answOne /= matrL[i][j];
                }
                else{
                    answOne -= matrL[i][j] * tmpY[j];
                }
            }
            tmpY[i] = answOne;
        }

        double[] answer = new double[matrU.length];
        for (int i = answer.length - 1; i >= 0; i--){
            double answOne = tmpY[i];
            for (int j = answer.length - 1; j >= i ; j--){
                if(j == i){
                    answOne /= matrU[i][j];
                }
                else{
                    answOne -= matrU[i][j] * answer[j];
                }
            }
            answer[i] = answOne;
        }

        System.out.println("Рішення системи через LU-розкладання: ");
        printLikeAnswerForLinearEquation.print(answer);

    }

    // Оскільки метод LU-розкладання використовується лише для визначених систем, то потрібно перевірити чи є система визначеною
    private static boolean hasOneSolution(double[][] coefficients, double[] constants){
        double[][] matr = new double[coefficients.length][coefficients[0].length + 1];

        // створюємо одну матрицю з коефіціентами та константами
        for (int i = 0; i < matr.length; i++){
            for (int j = 0; j < matr[0].length; j++){
                matr[i][j] = j < matr[0].length - 1 ? coefficients[i][j] : constants[i];
            }
        }

        // Приводимо до трикутного вигляду
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

        if (rank != rankExpanded) {
            System.out.println("Система рівнянь не має розв'язків");
            return false;
        }

        if(rank != coefficients.length){
            System.out.println("Система рівнянь невизначена (має багато розв'язків). Цей метод не використовується для знаходження загального рішення");
            return false;
        }

        return true;
    }
}
