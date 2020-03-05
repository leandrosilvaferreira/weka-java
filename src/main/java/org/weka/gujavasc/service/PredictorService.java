package org.weka.gujavasc.service;

import lombok.Getter;
import lombok.extern.java.Log;
import org.weka.gujavasc.model.Personagem;
import org.weka.gujavasc.util.FileUtils;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Log
public class PredictorService {

  public static final double FATOR_MINIMO_REJEICAO = 0.60;

  private Instances instancias;

  private String resultNaiveBart;
  private String resultNaiveHomer;

  private String resultJ48Bart;
  private String resultJ48Homer;

  private String resultOneRBart;
  private String resultOneRHomer;

  private String resultJRipBart;
  private String resultJRipHomer;

  private String resultIBkBart;
  private String resultIBkHomer;

  private String resultSVMBart;
  private String resultSVMHomer;

  private String resultMultiLayerBart;
  private String resultMultiLayerHomer;

  public void carregaBaseWeka() throws Exception {

    final DataSource ds = new DataSource(FileUtils.getResoucePath("caracteristicas.arff"));
    this.instancias = ds.getDataSet();
    this.instancias.setClassIndex(this.instancias.numAttributes() - 1);
  }

  private Instance buildInstance(final Personagem personagem) {

    final Instance novo = new DenseInstance(this.instancias.numAttributes());

    novo.setDataset(this.instancias);
    novo.setValue(0, personagem.getLaranjaCamisaBart());
    novo.setValue(1, personagem.getAzulCalcaoBart());
    novo.setValue(2, personagem.getAzulSapatoBart());
    novo.setValue(3, personagem.getMarromBocaHomer());
    novo.setValue(4, personagem.getAzulCalcaHomer());
    novo.setValue(5, personagem.getCinzaSapatoHomer());

    return novo;
  }

  public String classificar(final Personagem personagem) throws Exception {

    final DecimalFormat df = new DecimalFormat(" #,##0.00 %");
    final AtomicInteger quantidadeHomer = new AtomicInteger();
    final AtomicInteger quantidadeBart = new AtomicInteger();

    // Instância a ser classificada
    final Instance novo = this.buildInstance(personagem);

    this.classificaNaiveBayes(df, quantidadeHomer, quantidadeBart, novo);

    this.classificaJ48(df, quantidadeHomer, quantidadeBart, novo);

    this.classificaOneR(df, quantidadeHomer, quantidadeBart, novo);

    this.classificaJRip(df, quantidadeHomer, quantidadeBart, novo);

    this.classificaIBK(df, quantidadeHomer, quantidadeBart, novo);

    this.classificaLibSVM(df, quantidadeHomer, quantidadeBart, novo);

    this.classificaMultiLayerPerceptron(df, quantidadeHomer, quantidadeBart, novo);

    if (quantidadeBart.get() == 0 && quantidadeHomer.get() == 0) {
      return "A instância foi rejeitada";
    }
    return quantidadeBart.get() > quantidadeHomer.get() ? "A classe é Bart" : "A classe é Homer";
  }

  /**
   * weka.classifiers.functions.MultilayerPerceptron
   *
   * <p>A classifier that uses backpropagation to learn a multi-layer perceptron to classify *
   * instances. The network can be built by hand or or set up using a simple heuristic. The *
   * network parameters can also be monitored and modified during training time. The nodes in this *
   * network are all sigmoid (except for when the class is numeric, in which case the output nodes *
   * become unthresholded linear units).
   *
   * @param df
   * @param quantidadeHomer
   * @param quantidadeBart
   * @param novo
   * @throws Exception
   */
  private void classificaMultiLayerPerceptron(
      final DecimalFormat df,
      final AtomicInteger quantidadeHomer,
      final AtomicInteger quantidadeBart,
      final Instance novo)
      throws Exception {

    log.info("Gerando modelo MultilayerPerceptron");
    final MultilayerPerceptron multi = new MultilayerPerceptron();
    multi.buildClassifier(this.instancias);

    log.info("Classificando instância utilizando  MultiLayerPerceptron");
    final double[] resultadoMultiLayer = multi.distributionForInstance(novo);
    this.processaResultadoPredicao(quantidadeHomer, quantidadeBart, resultadoMultiLayer);
    this.resultMultiLayerBart = "Bart: " + df.format(resultadoMultiLayer[0]);
    this.resultMultiLayerHomer = "Homer: " + df.format(resultadoMultiLayer[1]);
  }

  /**
   * weka.classifiers.functions.LibSVM
   *
   * <p>A wrapper class for the libsvm library. This wrapper supports the classifiers implemented *
   * in the libsvm library, including one-class SVMs. Note: To be consistent with other SVMs in *
   * WEKA, the target attribute is now normalized before SVM regression is performed, if *
   * normalization is turned on.
   *
   * @param df
   * @param quantidadeHomer
   * @param quantidadeBart
   * @param novo
   * @throws Exception
   */
  private void classificaLibSVM(
      final DecimalFormat df,
      final AtomicInteger quantidadeHomer,
      final AtomicInteger quantidadeBart,
      final Instance novo)
      throws Exception {

    log.info("Gerando modelo LibSVM");
    final LibSVM svm = new LibSVM();
    svm.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_LINEAR, LibSVM.TAGS_KERNELTYPE));
    svm.buildClassifier(this.instancias);

    log.info("Classificando instância utilizando  LibSVM");
    final double[] resultadoSVM = svm.distributionForInstance(novo);
    this.processaResultadoPredicao(quantidadeHomer, quantidadeBart, resultadoSVM);
    this.resultSVMBart = "Bart: " + df.format(resultadoSVM[0]);
    this.resultSVMHomer = "Homer: " + df.format(resultadoSVM[1]);
  }

  /**
   * weka.classifiers.lazy.IBk
   *
   * <p>K-nearest neighbours classifier. Can select appropriate value of K based on *
   * cross-validation. Can also do distance weighting.
   *
   * @param df
   * @param quantidadeHomer
   * @param quantidadeBart
   * @param novo
   * @throws Exception
   */
  private void classificaIBK(
      final DecimalFormat df,
      final AtomicInteger quantidadeHomer,
      final AtomicInteger quantidadeBart,
      final Instance novo)
      throws Exception {

    log.info("Gerando modelo IBk");
    final IBk ibk = new IBk(3);
    ibk.buildClassifier(this.instancias);

    log.info("Classificando instância utilizando  IBK");
    final double[] resultadoIbk = ibk.distributionForInstance(novo);
    this.processaResultadoPredicao(quantidadeHomer, quantidadeBart, resultadoIbk);
    this.resultIBkBart = "Bart: " + df.format(resultadoIbk[0]);
    this.resultIBkHomer = "Homer: " + df.format(resultadoIbk[1]);
  }

  /**
   * weka.classifiers.rules.JRip
   *
   * <p>This class implements a propositional rule learner, Repeated Incremental Pruning to *
   * Produce Error Reduction (RIPPER), which was proposed by William W. Cohen as an optimized *
   * version of IREP.
   *
   * @param df
   * @param quantidadeHomer
   * @param quantidadeBart
   * @param novo
   * @throws Exception
   */
  private void classificaJRip(
      final DecimalFormat df,
      final AtomicInteger quantidadeHomer,
      final AtomicInteger quantidadeBart,
      final Instance novo)
      throws Exception {

    log.info("Gerando modelo JRip");
    final JRip jrip = new JRip();
    jrip.buildClassifier(this.instancias);

    log.info("Classificando instância utilizando  JRIP");
    final double[] resultadoJRip = jrip.distributionForInstance(novo);
    this.processaResultadoPredicao(quantidadeHomer, quantidadeBart, resultadoJRip);
    this.resultJRipBart = "Bart: " + df.format(resultadoJRip[0]);
    this.resultJRipHomer = "Homer: " + df.format(resultadoJRip[1]);
  }

  /**
   * weka.classifiers.rules.OneR
   *
   * <p>Class for building and using a 1R classifier; in other words, uses the minimum-error *
   * attribute for prediction, discretizing numeric attributes.
   *
   * @param df
   * @param quantidadeHomer
   * @param quantidadeBart
   * @param novo
   * @throws Exception
   */
  private void classificaOneR(
      final DecimalFormat df,
      final AtomicInteger quantidadeHomer,
      final AtomicInteger quantidadeBart,
      final Instance novo)
      throws Exception {

    log.info("Gerando modelo OneR");
    final OneR oner = new OneR();
    oner.buildClassifier(this.instancias);

    log.info("Classificando instância utilizando  ONER");
    final double[] resultadoOneR = oner.distributionForInstance(novo);
    this.processaResultadoPredicao(quantidadeHomer, quantidadeBart, resultadoOneR);
    this.resultOneRBart = "Bart: " + df.format(resultadoOneR[0]);
    this.resultOneRHomer = "Homer: " + df.format(resultadoOneR[1]);
  }

  /**
   * weka.classifiers.trees.J48
   *
   * <p>Class for generating a pruned or unpruned C4.5 decision tree
   *
   * @param df
   * @param quantidadeHomer
   * @param quantidadeBart
   * @param novo
   * @throws Exception
   */
  private void classificaJ48(
      final DecimalFormat df,
      final AtomicInteger quantidadeHomer,
      final AtomicInteger quantidadeBart,
      final Instance novo)
      throws Exception {

    log.info("Gerando modelo J48");
    final J48 arvore = new J48();
    arvore.buildClassifier(this.instancias);

    log.info("Classificando instância utilizando  J48");
    final double[] resultadoJ48 = arvore.distributionForInstance(novo);
    this.processaResultadoPredicao(quantidadeHomer, quantidadeBart, resultadoJ48);
    this.resultJ48Bart = "Bart: " + df.format(resultadoJ48[0]);
    this.resultJ48Homer = "Homer: " + df.format(resultadoJ48[1]);
  }

  /**
   * weka.classifiers.bayes.NaiveBayes
   *
   * <p>Class for a Naive Bayes classifier using estimator classes. Numeric estimator precision *
   * values are chosen based on analysis of the training data.
   *
   * @param df
   * @param quantidadeHomer
   * @param quantidadeBart
   * @param novo
   * @throws Exception
   */
  private void classificaNaiveBayes(
      final DecimalFormat df,
      final AtomicInteger quantidadeHomer,
      final AtomicInteger quantidadeBart,
      final Instance novo)
      throws Exception {

    log.info("Gerando modelo NaiveBayes");
    final NaiveBayes nb = new NaiveBayes();
    nb.buildClassifier(this.instancias);

    log.info("Classificando instância utilizando  NAIVE BAYES");
    final double[] resultadoNaive = nb.distributionForInstance(novo);
    this.processaResultadoPredicao(quantidadeHomer, quantidadeBart, resultadoNaive);
    this.resultNaiveBart = "Bart: " + df.format(resultadoNaive[0]);
    this.resultNaiveHomer = "Homer: " + df.format(resultadoNaive[1]);
  }

  /**
   * Calcula o resultado da predição levando em conta o fator mínimo de rejeição para a
   * classificação encontrada
   *
   * @param quantidadeHomer
   * @param quantidadeBart
   * @param resultado
   */
  private void processaResultadoPredicao(
      final AtomicInteger quantidadeHomer,
      final AtomicInteger quantidadeBart,
      final double[] resultado) {

    if (resultado[0] > resultado[1]) {
      if (resultado[0] > FATOR_MINIMO_REJEICAO) {
        quantidadeBart.addAndGet(1);
      }
    } else if (resultado[1] > resultado[0]) {
      if (resultado[1] > FATOR_MINIMO_REJEICAO) {
        quantidadeHomer.addAndGet(1);
      }
    } else if (resultado[0] > FATOR_MINIMO_REJEICAO && resultado[1] > FATOR_MINIMO_REJEICAO) {
      quantidadeBart.addAndGet(1);
      quantidadeHomer.addAndGet(1);
    }
  }

  public void classificarUsandoModelo(final Personagem personagem) {

    final AtomicInteger quantidadeHomer = new AtomicInteger();
    final AtomicInteger quantidadeBart = new AtomicInteger();

    try {
      final ObjectInputStream modeloJ48 =
          new ObjectInputStream(new FileInputStream(FileUtils.getResoucePath("modelos/j48.model")));
      final J48 j48 = (J48) modeloJ48.readObject();
      modeloJ48.close();

      final ObjectInputStream modeloJrip =
          new ObjectInputStream(
              new FileInputStream(FileUtils.getResoucePath("modelos/jrip.model")));
      final JRip jrip = (JRip) modeloJrip.readObject();
      modeloJrip.close();

      final ObjectInputStream modeloIbk =
          new ObjectInputStream(
              new FileInputStream(FileUtils.getResoucePath("modelos/ibk3.model")));
      final IBk ibk = (IBk) modeloIbk.readObject();
      modeloIbk.close();

      this.carregaBaseWeka();

      final Instance novo = this.buildInstance(personagem);

      // Classificando instância utilizando
      double resultado[] = j48.distributionForInstance(novo);
      final DecimalFormat df = new DecimalFormat("#,###.0000");
      System.out.println("J48");
      System.out.println("Bart: " + df.format(resultado[0]));
      System.out.println("Homer: " + df.format(resultado[1]));

      this.processaResultadoPredicao(quantidadeHomer, quantidadeBart, resultado);

      resultado = jrip.distributionForInstance(novo);
      System.out.println("JRip");
      System.out.println("Bart: " + df.format(resultado[0]));
      System.out.println("Homer: " + df.format(resultado[1]));

      this.processaResultadoPredicao(quantidadeHomer, quantidadeBart, resultado);

      resultado = ibk.distributionForInstance(novo);
      System.out.println("IBK 3");
      System.out.println("Bart: " + df.format(resultado[0]));
      System.out.println("Homer: " + df.format(resultado[1]));

      this.processaResultadoPredicao(quantidadeHomer, quantidadeBart, resultado);

      System.out.println("Quantidade Bart: " + quantidadeBart);
      System.out.println("Quantidade Homer: " + quantidadeHomer);

      if (quantidadeBart.get() == 0 && quantidadeHomer.get() == 0) {
        System.out.println("A instância foi rejeitada");
      } else if (quantidadeBart.get() > quantidadeHomer.get()) {
        System.out.println("A classe é Bart");
      } else {
        System.out.println("A classe é Homer");
      }

    } catch (final IOException ex) {
      Logger.getLogger(PredictorService.class.getName()).log(Level.SEVERE, null, ex);
    } catch (final ClassNotFoundException ex) {
      Logger.getLogger(PredictorService.class.getName()).log(Level.SEVERE, null, ex);
    } catch (final Exception ex) {
      Logger.getLogger(PredictorService.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void treinarModeloSalvandoArquivo() throws Exception {

    this.carregaBaseWeka();

    final J48 j48 = new J48();
    j48.buildClassifier(this.getInstancias());

    final ObjectOutputStream classificador =
        new ObjectOutputStream(
            new FileOutputStream(FileUtils.getResourcesAbsolutPath() + "/modelos/j48.model"));

    classificador.writeObject(j48);
    classificador.flush();
    classificador.close();
  }
}
