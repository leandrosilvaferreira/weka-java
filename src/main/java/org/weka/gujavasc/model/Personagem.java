package org.weka.gujavasc.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.opencv.opencv_core.CvScalar;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.weka.gujavasc.util.RgbUtils;

import static org.bytedeco.opencv.global.opencv_core.cvGet2D;

/**
 * Classe que representa um personagem (Homer ou Simpson) e suas devidas características extraídas
 * do arquivo de imagem
 */
public class Personagem {

  public static final int PERSONAGEM_BART_COD = 0;
  public static final String PERSONAGEM_BART_DESC = "Bart";

  public static final int PERSONAGEM_HOMER_COD = 1;
  public static final String PERSONAGEM_HOMER_DESC = "Homer";

  private final IplImage imagem;

  @Getter private Integer classePersonagem;
  @Getter private String classePersonagemDesc;

  @Getter @Setter private float laranjaCamisaBart;
  @Getter @Setter private float azulCalcaoBart;
  @Getter @Setter private float azulSapatoBart;

  @Getter @Setter private float azulCalcaHomer;
  @Getter @Setter private float marromBocaHomer;
  @Getter @Setter private float cinzaSapatoHomer;

  public Personagem() {
    this.imagem = null;
  }

  public Personagem(final IplImage imagem) {
    this(imagem, null);
  }

  public Personagem(@NonNull final IplImage imagem, final String nomeImagem) {
    this.imagem = imagem;
    this.extraiClassePersonagemPeloNomeDaImagem(nomeImagem);
    this.contarCores();
    this.normalizarCoresProporcionalmenteAoTamanhoDaImagem(this.imagem);
  }

  /**
   * Definição da classe (Homer ou Bart) a partir do nome da imagem
   *
   * @param nomeImagem
   */
  private void extraiClassePersonagemPeloNomeDaImagem(final String nomeImagem) {

    // O nome da imagem será utilizado somente na etapa de treinamento
    if (nomeImagem == null) {
      return;
    }

    if (nomeImagem.toUpperCase().startsWith(StringUtils.left(PERSONAGEM_BART_DESC, 1))) {

      this.classePersonagem = PERSONAGEM_BART_COD;
      this.classePersonagemDesc = PERSONAGEM_BART_DESC;

    } else {

      this.classePersonagem = PERSONAGEM_HOMER_COD;
      this.classePersonagemDesc = PERSONAGEM_HOMER_DESC;
    }
  }

  /**
   * Varre a imagem pixel a pixel identificando as cores de interesse para cada pixel
   *
   * @return
   */
  private void contarCores() {

    this.laranjaCamisaBart = 0;
    this.azulCalcaoBart = 0;
    this.azulSapatoBart = 0;
    this.azulCalcaHomer = 0;
    this.marromBocaHomer = 0;
    this.cinzaSapatoHomer = 0;

    for (int altura = 0; altura < this.imagem.height(); altura++) {

      for (int largura = 0; largura < this.imagem.width(); largura++) {

        // Extração do RGB de cada pixel da imagem
        final CvScalar pixelRGB = cvGet2D(this.imagem, altura, largura);

        if (RgbUtils.isLaranjaCamisaBart(pixelRGB)) {
          this.laranjaCamisaBart++;
        }

        // Calção azul do Bart (metade de baixo da imagem)
        if (altura > (this.imagem.height() / 2)) {
          if (RgbUtils.isAzulCalcaoBart(pixelRGB)) {
            this.azulCalcaoBart++;
          }
        }

        // Sapato do Bart (parte inferior da imagem)
        if (altura > (this.imagem.height() / 2) + (this.imagem.height() / 3)) {
          if (RgbUtils.isAzulSapatoBart(pixelRGB)) {
            this.azulSapatoBart++;
          }
        }

        // Calça azul do Homer
        if (RgbUtils.isAzulCalcaHomer(pixelRGB)) {
          this.azulCalcaHomer++;
        }

        // Boca do Homer (pouco mais da metade da imagem)
        if (altura < (this.imagem.height() / 2) + (this.imagem.height() / 3)) {
          if (RgbUtils.isMarromBocaHomer(pixelRGB)) {
            this.marromBocaHomer++;
          }
        }

        // Sapato do Homer (parte inferior da imagem)
        if (altura > (this.imagem.height() / 2) + (this.imagem.height() / 3)) {
          if (RgbUtils.isPretoSapatoHomer(pixelRGB)) {
            this.cinzaSapatoHomer++;
          }
        }
      }
    }
  }

  /**
   * Normaliza as características pelo número de pixels totais da imagem
   *
   * @param imagem
   */
  private void normalizarCoresProporcionalmenteAoTamanhoDaImagem(final IplImage imagem) {

    this.laranjaCamisaBart = (this.laranjaCamisaBart / (imagem.height() * imagem.width())) * 100;
    this.azulCalcaoBart = (this.azulCalcaoBart / (imagem.height() * imagem.width())) * 100;
    this.azulSapatoBart = (this.azulSapatoBart / (imagem.height() * imagem.width())) * 100;
    this.azulCalcaHomer = (this.azulCalcaHomer / (imagem.height() * imagem.width())) * 100;
    this.marromBocaHomer = (this.marromBocaHomer / (imagem.height() * imagem.width())) * 100;
    this.cinzaSapatoHomer = (this.cinzaSapatoHomer / (imagem.height() * imagem.width())) * 100;
  }

  @Override
  public String toString() {

    final String pattern =
        "Bart  :  Laranja camisa : %18.15f - Azul calção : %18.15f - Azul sapato  : %18.15f%nHomer :  Azul calça     : %18.15f - Marrom boca : %18.15f - Cinza sapato : %18.15f%nClasse: %d (%s) %n%n%n";

    return String.format(
        pattern,
        this.getLaranjaCamisaBart(),
        this.getAzulCalcaoBart(),
        this.getAzulSapatoBart(),
        this.getAzulCalcaHomer(),
        this.getMarromBocaHomer(),
        this.getCinzaSapatoHomer(),
        this.getClassePersonagem() != null ? this.getClassePersonagem() : 99,
        this.getClassePersonagemDesc() != null ? this.getClassePersonagemDesc() : "");
  }
}
