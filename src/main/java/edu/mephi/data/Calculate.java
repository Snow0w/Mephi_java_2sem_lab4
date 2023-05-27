package edu.mephi.data;

import java.util.ArrayList;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class Calculate {
  private final double quantile = 1.69;

  private Result geometricMean(double[] arr) {
    Result out = new Result();
    out.name = new String("Среднее геометрическое");
    out.value = StatUtils.geometricMean(arr);
    return out;
  }

  private Result arithmeticMean(double[] arr) {
    Result out = new Result();
    out.name = new String("Среднее арифметическое");
    out.value = StatUtils.mean(arr);
    return out;
  }

  private Result totalNumber(double[] arr) {
    Result out = new Result();
    out.name = new String("Размер выборки");
    out.value = arr.length;
    return out;
  }

  private Result standardDeviation(double[] arr) {
    Result out = new Result();
    StandardDeviation s = new StandardDeviation();
    out.name = new String("Оценка стандартного отклонения");
    out.value = s.evaluate(arr);
    return out;
  }

  private Result max(double[] arr) {
    Result out = new Result();
    out.name = new String("Максимум");
    out.value = StatUtils.max(arr);
    return out;
  }

  private Result min(double[] arr) {
    Result out = new Result();
    out.name = new String("Минимум");
    out.value = StatUtils.min(arr);
    return out;
  }

  private Result variance(double[] arr) {
    Result out = new Result();
    out.name = new String("Оценка дисперсии");
    out.value = StatUtils.populationVariance(arr);
    return out;
  }

  private Result range(double[] arr) {
    Result out = new Result();
    out.name = new String("Размах выборки");
    out.value = StatUtils.max(arr) - StatUtils.min(arr);
    return out;
  }

  private Result coeffVariance(double[] arr) {
    Result out = new Result();
    StandardDeviation s = new StandardDeviation();
    out.value = s.evaluate(arr);
    out.name = new String("Коэффициент вариации");
    out.value = s.evaluate(arr) / StatUtils.mean(arr);
    return out;
  }

  private Result interval(double[] arr) {
    Result out = new Result();
    out.name = new String("Доверительный интервал для мат ожидания");
    double mean = StatUtils.mean(arr);
    StandardDeviation s = new StandardDeviation();
    double sd = s.evaluate(arr);
    double lowBound = mean - quantile * sd / Math.sqrt(arr.length);
    double highBound = mean + quantile * sd / Math.sqrt(arr.length);
    out.value = new String("[" + Double.toString(lowBound) + "; " +
                           Double.toString(highBound) + "]");

    return out;
  }

  public ArrayList<Result> calcPerArray(double[] arr) {
    ArrayList<Result> out = new ArrayList<Result>();
    out.add(totalNumber(arr));
    out.add(arithmeticMean(arr));
    Result gmin = min(arr);
    if ((Double)gmin.value > 0)
      out.add(geometricMean(arr));
    out.add(standardDeviation(arr));
    out.add(variance(arr));
    out.add(max(arr));
    out.add(gmin);
    out.add(range(arr));
    out.add(coeffVariance(arr));
    out.add(interval(arr));

    return out;
  }

  public ArrayList<Result> calcCov(double[] x, double[] y, double[] z) {
    ArrayList<Result> out = new ArrayList<Result>();
    Covariance cov = new Covariance();
    out.add(new Result("XY", cov.covariance(x, y)));
    out.add(new Result("XZ", cov.covariance(x, z)));
    out.add(new Result("YZ", cov.covariance(y, z)));

    return out;
  }
}
