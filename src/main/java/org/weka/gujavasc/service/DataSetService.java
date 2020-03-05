package org.weka.gujavasc.service;

import org.bytedeco.opencv.opencv_core.IplImage;
import org.weka.gujavasc.model.Personagem;
import org.weka.gujavasc.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

public class DataSetService {

  public static final String PATH_ARQUIVO_ARFF =
      Paths.get("").toAbsolutePath() + "/src/main/resources/caracteristicas.arff";

  public String generateArff() throws IOException {

    final StringBuilder bufferArffFile = new StringBuilder();

    // Cabeçalho do arquivo ARFF
    bufferArffFile
        .append("@relation caracteristicas\n\n")
        .append("@attribute laranja_camisa_bart real\n")
        .append("@attribute azul_calcao_bart real\n")
        .append("@attribute azul_sapato_bart real\n")
        .append("@attribute marrom_boca_homer real\n")
        .append("@attribute azul_calca_homer real\n")
        .append("@attribute cinza_sapato_homer real\n")
        .append("@attribute classe {Bart, Homer}\n\n")
        .append("@data\n");

    final List<Personagem> personagens = new ArrayList<>();

    // Lista contendo todos os arquivos de imagens para treinamento do modelo
    final List<File> listaImagemFiles = FileUtils.getResourceFolderFiles("imagens");

    listaImagemFiles
        .parallelStream()
        .forEach(
            imagemFile -> {
              final IplImage imagem = cvLoadImage(imagemFile.getAbsolutePath());

              final Personagem personagem = new Personagem(imagem, imagemFile.getName());

              personagens.add(personagem);

              System.out.println(personagem.toString());

              bufferArffFile
                  .append(personagem.getLaranjaCamisaBart())
                  .append(",")
                  .append(personagem.getAzulCalcaoBart())
                  .append(",")
                  .append(personagem.getAzulSapatoBart())
                  .append(",")
                  .append(personagem.getAzulCalcaHomer())
                  .append(",")
                  .append(personagem.getMarromBocaHomer())
                  .append(",")
                  .append(personagem.getCinzaSapatoHomer())
                  .append(",")
                  .append(personagem.getClassePersonagemDesc())
                  .append("\n");
            });

    return FileUtils.salvarArquivo(PATH_ARQUIVO_ARFF, bufferArffFile.toString());
  }
}
