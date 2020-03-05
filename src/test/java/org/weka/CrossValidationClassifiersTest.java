package org.weka;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.weka.gujavasc.util.FileUtils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.J48;
import weka.core.Debug.Random;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;

import static org.weka.gujavasc.service.PredictorService.FATOR_MINIMO_REJEICAO;

public class CrossValidationClassifiersTest {

  private final int folds = 10;
  private final int vezes = 1;

  private DataSource fonte;

  private static void crossValidate(
      final DataSource fonte, final int folds, final int vezes, final Classifier classificador)
      throws Exception {

    final Instances dados = fonte.getDataSet();
    dados.setClassIndex(dados.numAttributes() - 1);

    for (int i = 1; i <= vezes; i++) {
      final Evaluation avaliador = new Evaluation(dados);
      avaliador.crossValidateModel(classificador, dados, folds, new Random(i));

      System.out.println(avaliador.toSummaryString());
      System.out.println(avaliador.toMatrixString("Matrix de Confusão"));

      Assert.assertTrue(
          "Pct Correto menor que " + FATOR_MINIMO_REJEICAO + "%: " + avaliador.pctCorrect(),
          avaliador.pctCorrect() > FATOR_MINIMO_REJEICAO);
    }
  }

  @Before
  public void before() throws Exception {
    this.fonte = new DataSource(FileUtils.getResoucePath("caracteristicas.arff"));
  }

  @Test
  public void crossValidateNB() throws Exception {
    System.out.println(" ");
    System.out.println("Crossvalidation: NaiveBayes");
    final Classifier classificadorNB = new NaiveBayes();
    crossValidate(this.fonte, this.folds, this.vezes, classificadorNB);
  }

  @Test
  public void crossValidateJ48() throws Exception {
    System.out.println("Crossvalidation: J48");
    final Classifier classificadorJ48 = new J48();
    crossValidate(this.fonte, this.folds, this.vezes, classificadorJ48);
  }

  @Test
  public void crossValidateOneR() throws Exception {
    System.out.println("Crossvalidation: OneR");
    final Classifier classificadorOneR = new OneR();
    crossValidate(this.fonte, this.folds, this.vezes, classificadorOneR);
  }

  @Test
  public void crossValidateJRip() throws Exception {
    System.out.println("Crossvalidation: JRip");
    final JRip jrip = new JRip();
    jrip.setUsePruning(false);
    final Classifier classificadorJRip = jrip;
    crossValidate(this.fonte, this.folds, this.vezes, classificadorJRip);
  }

  @Test
  public void crossValidateIBK() throws Exception {
    System.out.println("Crossvalidation: IBk");
    final Classifier classificadorIBK = new IBk(3);
    crossValidate(this.fonte, this.folds, this.vezes, classificadorIBK);
  }

  @Test
  public void crossValidateLibSVM() throws Exception {
    System.out.println("Crossvalidation: LibSVM");
    final LibSVM svm = new LibSVM();
    svm.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_LINEAR, LibSVM.TAGS_KERNELTYPE));
    final Classifier classificadorLibSVM = svm;
    crossValidate(this.fonte, this.folds, this.vezes, classificadorLibSVM);
  }

  @Test
  public void crossValidateMLP() throws Exception {
    System.out.println("Crossvalidation: MultilayerPerceptron");
    final Classifier classificadorMLP = new MultilayerPerceptron();
    crossValidate(this.fonte, this.folds, this.vezes, classificadorMLP);
  }
}
