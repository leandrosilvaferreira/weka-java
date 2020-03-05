package org.weka.gujavasc;

import org.bytedeco.opencv.opencv_core.IplImage;
import org.weka.gujavasc.model.Personagem;
import org.weka.gujavasc.service.DataSetService;
import org.weka.gujavasc.service.PredictorService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

public class PredictorUI extends JFrame {

  private static final long serialVersionUID = -6593665984511555265L;

  private Personagem personagem;
  private PredictorService predictorService;

  private JButton btnClassificar;
  private JButton btnExtrairCaracteristicas;
  private JButton btnSelecionarImagem;
  private JButton jButtonExtrairArr;
  private JButton jButton2;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabelAlgoNaive;
  private JLabel jLabelAlgoJ48;
  private JLabel jLabelAlgoOneR;
  private JLabel jLabelAlgoJRip;
  private JLabel jLabelAlgoIbk;
  private JLabel jLabelAlgoSvm;
  private JLabel jLabelAlgoMultilayer;
  private JLabel lblAzulCalcao;
  private JLabel lblAzulHomer;
  private JLabel lblAzulSapato;
  private JLabel lblIBkBart;
  private JLabel lblIBkHomer;
  private JLabel lblImagem;
  private JLabel lblJ48Bart;
  private JLabel lblJ48Homer;
  private JLabel lblJRipBart;
  private JLabel lblJRipHomer;
  private JLabel lblLaranjaBart;
  private JLabel lblMarromHomer;
  private JLabel lblMultiLayerBart;
  private JLabel lblMultiLayerHomer;
  private JLabel lblNaiveBart;
  private JLabel lblNaiveHomer;
  private JLabel lblOneRBart;
  private JLabel lblOneRHomer;
  private JLabel lblSVMBart;
  private JLabel lblSVMHomer;
  private JLabel lblSapatoHomer;
  private JTextField txtCaminhoImagem;

  public PredictorUI() {
    this.initComponents();
  }

  public static void main(final String[] args) {

    EventQueue.invokeLater(() -> new PredictorUI().setVisible(true));
  }

  private void btnSelecionarImagemActionPerformed(final java.awt.event.ActionEvent evt) {

    final JFileChooser fc = new JFileChooser();
    final int retorno = fc.showDialog(this, "Selecione a imagem");

    if (retorno == JFileChooser.APPROVE_OPTION) {
      final File arquivo = fc.getSelectedFile();
      this.txtCaminhoImagem.setText(arquivo.getAbsolutePath());

      BufferedImage imagemBmp = null;
      try {
        imagemBmp = ImageIO.read(arquivo);
      } catch (final IOException ex) {
        Logger.getLogger(PredictorUI.class.getName()).log(Level.SEVERE, null, ex);
      }

      final ImageIcon imagemLabel = new ImageIcon(imagemBmp);

      this.lblImagem.setIcon(
          new ImageIcon(
              imagemLabel
                  .getImage()
                  .getScaledInstance(
                      this.lblImagem.getWidth(), this.lblImagem.getHeight(), Image.SCALE_DEFAULT)));
    }
  }

  private void btnExtrairCaracteristicasActionPerformed(final java.awt.event.ActionEvent evt) {

    final IplImage imagem = cvLoadImage(this.txtCaminhoImagem.getText());
    this.personagem = new Personagem(imagem);

    this.lblLaranjaBart.setText(String.valueOf(this.personagem.getLaranjaCamisaBart()));
    this.lblAzulCalcao.setText(String.valueOf(this.personagem.getAzulCalcaoBart()));
    this.lblAzulSapato.setText(String.valueOf(this.personagem.getAzulSapatoBart()));
    this.lblAzulHomer.setText(String.valueOf(this.personagem.getAzulCalcaHomer()));
    this.lblMarromHomer.setText(String.valueOf(this.personagem.getMarromBocaHomer()));
    this.lblSapatoHomer.setText(String.valueOf(this.personagem.getCinzaSapatoHomer()));
  }

  private void btnClassificarActionPerformed(final java.awt.event.ActionEvent evt) {
    try {

      if (this.personagem == null) {
        System.out.println("Personagem nulo!!!");
        return;
      }

      this.predictorService.carregaBaseWeka();

      final String resultado = this.predictorService.classificar(this.personagem);

      this.lblNaiveBart.setText(this.predictorService.getResultNaiveBart());
      this.lblNaiveHomer.setText(this.predictorService.getResultNaiveHomer());

      this.lblJ48Bart.setText(this.predictorService.getResultJ48Bart());
      this.lblJ48Homer.setText(this.predictorService.getResultJ48Homer());

      this.lblOneRBart.setText(this.predictorService.getResultOneRBart());
      this.lblOneRHomer.setText(this.predictorService.getResultOneRHomer());

      this.lblJRipBart.setText(this.predictorService.getResultJRipBart());
      this.lblJRipHomer.setText(this.predictorService.getResultJRipHomer());

      this.lblIBkBart.setText(this.predictorService.getResultIBkBart());
      this.lblIBkHomer.setText(this.predictorService.getResultIBkHomer());

      this.lblSVMBart.setText(this.predictorService.getResultSVMBart());
      this.lblSVMHomer.setText(this.predictorService.getResultSVMHomer());

      this.lblMultiLayerBart.setText(this.predictorService.getResultMultiLayerBart());
      this.lblMultiLayerHomer.setText(this.predictorService.getResultMultiLayerHomer());

      JOptionPane.showMessageDialog(null, resultado);

    } catch (final Exception ex) {
      Logger.getLogger(PredictorUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void jButtonExtrairArrActionPerformed(final java.awt.event.ActionEvent evt) {
    try {

      final DataSetService dataSetService = new DataSetService();

      final String resultado = dataSetService.generateArff();

      JOptionPane.showMessageDialog(null, resultado);

    } catch (final Exception ex) {
      Logger.getLogger(PredictorUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) {

    final Personagem personagem = new Personagem();
    personagem.setLaranjaCamisaBart(Float.parseFloat(this.lblLaranjaBart.getText()));
    personagem.setAzulCalcaoBart(Float.parseFloat(this.lblAzulCalcao.getText()));
    personagem.setAzulSapatoBart(Float.parseFloat(this.lblAzulSapato.getText()));
    personagem.setMarromBocaHomer(Float.parseFloat(this.lblMarromHomer.getText()));
    personagem.setAzulCalcaHomer(Float.parseFloat(this.lblAzulHomer.getText()));
    personagem.setCinzaSapatoHomer(Float.parseFloat(this.lblSapatoHomer.getText()));

    this.predictorService.classificarUsandoModelo(personagem);
  }

  private void initComponents() {

    this.predictorService = new PredictorService();

    this.btnSelecionarImagem = new JButton();
    this.txtCaminhoImagem = new JTextField();
    this.lblImagem = new JLabel();
    this.btnExtrairCaracteristicas = new JButton();
    this.jLabel1 = new JLabel();
    this.jLabel2 = new JLabel();
    this.lblLaranjaBart = new JLabel();
    this.lblAzulCalcao = new JLabel();
    this.lblAzulSapato = new JLabel();
    this.lblAzulHomer = new JLabel();
    this.lblMarromHomer = new JLabel();
    this.lblSapatoHomer = new JLabel();
    this.btnClassificar = new JButton();
    this.jLabelAlgoNaive = new JLabel();
    this.lblNaiveBart = new JLabel();
    this.lblNaiveHomer = new JLabel();
    this.jLabelAlgoJ48 = new JLabel();
    this.lblJ48Bart = new JLabel();
    this.lblJ48Homer = new JLabel();
    this.jLabelAlgoOneR = new JLabel();
    this.lblOneRBart = new JLabel();
    this.lblOneRHomer = new JLabel();
    this.jLabelAlgoJRip = new JLabel();
    this.lblJRipBart = new JLabel();
    this.lblJRipHomer = new JLabel();
    this.jLabelAlgoIbk = new JLabel();
    this.lblIBkBart = new JLabel();
    this.lblIBkHomer = new JLabel();
    this.jLabelAlgoSvm = new JLabel();
    this.lblSVMBart = new JLabel();
    this.lblSVMHomer = new JLabel();
    this.jLabelAlgoMultilayer = new JLabel();
    this.lblMultiLayerBart = new JLabel();
    this.lblMultiLayerHomer = new JLabel();
    this.jButtonExtrairArr = new JButton();
    this.jButton2 = new JButton();

    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    this.btnSelecionarImagem.setText("Selecionar imagem");

    this.lblImagem.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
    this.btnExtrairCaracteristicas.setText("Extrair características");

    this.jLabel1.setText("Características do Bart");
    this.jLabel2.setText("Características do Homer");

    this.lblLaranjaBart.setText("Laranja Camisa");
    this.lblLaranjaBart.setForeground(Color.blue);
    this.lblAzulCalcao.setText("Azul Calção");
    this.lblAzulCalcao.setForeground(Color.blue);
    this.lblAzulSapato.setText("Azul Sapato");
    this.lblAzulSapato.setForeground(Color.blue);

    this.lblAzulHomer.setText("Azul Calça");
    this.lblAzulHomer.setForeground(Color.blue);
    this.lblMarromHomer.setText("Marrom Boca");
    this.lblMarromHomer.setForeground(Color.blue);
    this.lblSapatoHomer.setText("Cinza Sapato");
    this.lblSapatoHomer.setForeground(Color.blue);

    this.btnClassificar.setText("Classificar");

    this.jLabelAlgoNaive.setText("Naive Bayes");
    this.lblNaiveBart.setText("");
    this.lblNaiveBart.setForeground(Color.blue);
    this.lblNaiveHomer.setText("");
    this.lblNaiveHomer.setForeground(Color.blue);

    this.jLabelAlgoJ48.setText("J48");
    this.lblJ48Bart.setText("");
    this.lblJ48Bart.setForeground(Color.blue);
    this.lblJ48Homer.setText("");
    this.lblJ48Homer.setForeground(Color.blue);

    this.jLabelAlgoOneR.setText("OneR");
    this.lblOneRBart.setText("");
    this.lblOneRBart.setForeground(Color.blue);
    this.lblOneRHomer.setText("");
    this.lblOneRHomer.setForeground(Color.blue);

    this.jLabelAlgoJRip.setText("JRip");
    this.lblJRipBart.setText("");
    this.lblJRipBart.setForeground(Color.blue);
    this.lblJRipHomer.setText("");
    this.lblJRipHomer.setForeground(Color.blue);

    this.jLabelAlgoIbk.setText("IBk");
    this.lblIBkBart.setText("");
    this.lblIBkBart.setForeground(Color.blue);
    this.lblIBkHomer.setText("");
    this.lblIBkHomer.setForeground(Color.blue);

    this.jLabelAlgoSvm.setText("SVM");
    this.lblSVMBart.setText("");
    this.lblSVMBart.setForeground(Color.blue);
    this.lblSVMHomer.setText("");
    this.lblSVMHomer.setForeground(Color.blue);

    this.jLabelAlgoMultilayer.setText("Multilayer");
    this.lblMultiLayerBart.setText("");
    this.lblMultiLayerBart.setForeground(Color.blue);
    this.lblMultiLayerHomer.setText("");
    this.lblMultiLayerHomer.setForeground(Color.blue);

    this.jButtonExtrairArr.setText("Extrar DataSet ARFF");
    this.jButton2.setText("Classificar usando modelo");
    this.jButton2.addActionListener(PredictorUI.this::jButton2ActionPerformed);
    this.jButton2.setVisible(false);

    this.jButtonExtrairArr.addActionListener(PredictorUI.this::jButtonExtrairArrActionPerformed);

    this.btnClassificar.addActionListener(PredictorUI.this::btnClassificarActionPerformed);

    this.btnExtrairCaracteristicas.addActionListener(
        PredictorUI.this::btnExtrairCaracteristicasActionPerformed);

    this.btnSelecionarImagem.addActionListener(
        PredictorUI.this::btnSelecionarImagemActionPerformed);

    final GroupLayout layout = new GroupLayout(this.getContentPane());

    this.getContentPane().setLayout(layout);

    layout.setHorizontalGroup(
        layout
            .createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        layout
                            .createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(
                                                this.lblImagem,
                                                GroupLayout.PREFERRED_SIZE,
                                                248,
                                                GroupLayout.PREFERRED_SIZE)
                                            .addGroup(
                                                layout
                                                    .createParallelGroup(
                                                        GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(
                                                        this.jButtonExtrairArr,
                                                        GroupLayout.Alignment.LEADING,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        Short.MAX_VALUE)
                                                    .addComponent(
                                                        this.jButton2,
                                                        GroupLayout.Alignment.LEADING,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        Short.MAX_VALUE)))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addGroup(
                                                layout
                                                    .createSequentialGroup()
                                                    .addComponent(this.btnExtrairCaracteristicas)
                                                    .addPreferredGap(
                                                        LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(
                                                        this.btnClassificar,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        118,
                                                        GroupLayout.PREFERRED_SIZE))
                                            .addGroup(
                                                layout
                                                    .createSequentialGroup()
                                                    .addGroup(
                                                        layout
                                                            .createParallelGroup(
                                                                GroupLayout.Alignment.LEADING)
                                                            .addComponent(this.jLabel1)
                                                            .addComponent(this.lblLaranjaBart)
                                                            .addComponent(this.lblAzulCalcao)
                                                            .addComponent(this.lblAzulSapato)
                                                            .addGroup(
                                                                layout
                                                                    .createSequentialGroup()
                                                                    .addGroup(
                                                                        layout
                                                                            .createParallelGroup(
                                                                                GroupLayout
                                                                                    .Alignment
                                                                                    .LEADING)
                                                                            .addComponent(
                                                                                this
                                                                                    .jLabelAlgoNaive)
                                                                            .addComponent(
                                                                                this.lblNaiveBart)
                                                                            .addComponent(
                                                                                this.lblNaiveHomer))
                                                                    .addGap(59, 59, 59)
                                                                    .addGroup(
                                                                        layout
                                                                            .createParallelGroup(
                                                                                GroupLayout
                                                                                    .Alignment
                                                                                    .LEADING)
                                                                            .addComponent(
                                                                                this.lblJ48Homer)
                                                                            .addComponent(
                                                                                this.lblJ48Bart)
                                                                            .addComponent(
                                                                                this.jLabelAlgoJ48)
                                                                            .addComponent(
                                                                                this.jLabelAlgoIbk)
                                                                            .addComponent(
                                                                                this.lblIBkBart)
                                                                            .addComponent(
                                                                                this.lblIBkHomer)))
                                                            .addGroup(
                                                                layout
                                                                    .createParallelGroup(
                                                                        GroupLayout.Alignment
                                                                            .TRAILING,
                                                                        false)
                                                                    .addComponent(
                                                                        this.lblJRipHomer,
                                                                        GroupLayout.Alignment
                                                                            .LEADING)
                                                                    .addGroup(
                                                                        layout
                                                                            .createParallelGroup(
                                                                                GroupLayout
                                                                                    .Alignment
                                                                                    .LEADING)
                                                                            .addComponent(
                                                                                this.jLabelAlgoJRip)
                                                                            .addComponent(
                                                                                this.lblJRipBart))))
                                                    .addGap(64, 64, 64)
                                                    .addGroup(
                                                        layout
                                                            .createParallelGroup(
                                                                GroupLayout.Alignment.LEADING)
                                                            .addGroup(
                                                                layout
                                                                    .createParallelGroup(
                                                                        GroupLayout.Alignment
                                                                            .TRAILING)
                                                                    .addComponent(
                                                                        this.lblSapatoHomer,
                                                                        GroupLayout.Alignment
                                                                            .LEADING)
                                                                    .addComponent(
                                                                        this.lblMarromHomer,
                                                                        GroupLayout.Alignment
                                                                            .LEADING)
                                                                    .addComponent(
                                                                        this.lblAzulHomer,
                                                                        GroupLayout.Alignment
                                                                            .LEADING)
                                                                    .addComponent(this.jLabel2))
                                                            .addGroup(
                                                                layout
                                                                    .createSequentialGroup()
                                                                    .addComponent(
                                                                        this.jLabelAlgoOneR)
                                                                    .addGap(130, 130, 130)
                                                                    .addComponent(
                                                                        this.jLabelAlgoSvm))
                                                            .addGroup(
                                                                layout
                                                                    .createSequentialGroup()
                                                                    .addComponent(this.lblOneRBart)
                                                                    .addGap(64, 64, 64)
                                                                    .addComponent(this.lblSVMBart))
                                                            .addGroup(
                                                                layout
                                                                    .createSequentialGroup()
                                                                    .addComponent(this.lblOneRHomer)
                                                                    .addGap(64, 64, 64)
                                                                    .addComponent(this.lblSVMHomer))
                                                            .addComponent(this.jLabelAlgoMultilayer)
                                                            .addComponent(this.lblMultiLayerBart)
                                                            .addComponent(
                                                                this.lblMultiLayerHomer)))))
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addComponent(this.btnSelecionarImagem)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(
                                        this.txtCaminhoImagem,
                                        GroupLayout.PREFERRED_SIZE,
                                        700,
                                        GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(34, Short.MAX_VALUE)));

    layout.setVerticalGroup(
        layout
            .createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        layout
                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(this.btnSelecionarImagem)
                            .addComponent(
                                this.txtCaminhoImagem,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(
                        layout
                            .createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addComponent(
                                        this.lblImagem,
                                        GroupLayout.PREFERRED_SIZE,
                                        256,
                                        GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(this.jButtonExtrairArr)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(this.jButton2))
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(this.btnExtrairCaracteristicas)
                                            .addComponent(this.btnClassificar))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(this.jLabel1)
                                            .addComponent(this.jLabel2))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(this.lblLaranjaBart)
                                            .addComponent(this.lblAzulHomer))
                                    .addGap(18, 18, 18)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(this.lblAzulCalcao)
                                            .addComponent(this.lblMarromHomer))
                                    .addGap(18, 18, 18)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(this.lblAzulSapato)
                                            .addComponent(this.lblSapatoHomer))
                                    .addGap(27, 27, 27)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(this.jLabelAlgoNaive)
                                            .addComponent(this.jLabelAlgoJ48)
                                            .addComponent(this.jLabelAlgoOneR)
                                            .addComponent(this.jLabelAlgoSvm))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(this.lblNaiveBart)
                                            .addComponent(this.lblJ48Bart)
                                            .addComponent(this.lblOneRBart)
                                            .addComponent(this.lblSVMBart))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(this.lblNaiveHomer)
                                            .addComponent(this.lblJ48Homer)
                                            .addComponent(this.lblOneRHomer)
                                            .addComponent(this.lblSVMHomer))
                                    .addGap(18, 18, 18)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(this.jLabelAlgoJRip)
                                            .addComponent(this.jLabelAlgoIbk)
                                            .addComponent(this.jLabelAlgoMultilayer))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(this.lblJRipBart)
                                            .addComponent(this.lblIBkBart)
                                            .addComponent(this.lblMultiLayerBart))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(this.lblJRipHomer)
                                            .addComponent(this.lblIBkHomer)
                                            .addComponent(this.lblMultiLayerHomer))))
                    .addContainerGap(71, Short.MAX_VALUE)));

    this.pack();
    this.setLocationRelativeTo(null);
  }
}
