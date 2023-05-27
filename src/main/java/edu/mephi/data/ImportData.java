package edu.mephi.data;

public class ImportData {
  private double[] xArray;
  private double[] yArray;
  private double[] zArray;

  public ImportData() {
    xArray = null;
    yArray = null;
    zArray = null;
  }
  public double[] getxArray() { return xArray; }
  public void setxArray(double[] xArray) { this.xArray = xArray; }
  public double[] getyArray() { return yArray; }
  public void setyArray(double[] yArray) { this.yArray = yArray; }
  public double[] getzArray() { return zArray; }
  public void setzArray(double[] zArray) { this.zArray = zArray; }
}
