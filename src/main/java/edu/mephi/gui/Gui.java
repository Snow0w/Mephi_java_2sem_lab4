package edu.mephi.gui;

import edu.mephi.data.ImportData;
import edu.mephi.excel.Excel;
import edu.mephi.exceptions.WrongExcelFileException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.commons.io.FilenameUtils;

public class Gui extends JFrame implements ActionListener {
  private static final int HEIGHT = 600;
  private static final int WIDTH = 800;
  private JButton importButton;
  private JButton exportButton;
  private JButton exitButton;
  private JPanel mainPane;
  private JPanel exitPane;

  private ImportData data;

  public Gui(String name) {
    super(name);
    this.setSize(WIDTH, HEIGHT);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setLayout(new BorderLayout());

    importButton = new JButton("Import excel file");
    exportButton = new JButton("Export data");
    exitButton = new JButton("Exit");
    mainPane = new JPanel(new GridLayout(2, 0));
    exitPane = new JPanel(new BorderLayout());
    importButton.addActionListener(this);
    exportButton.addActionListener(this);
    exitButton.addActionListener(this);

    mainPane.add(importButton);
    mainPane.add(exportButton);
    exitPane.add(exitButton);
    this.add(mainPane, BorderLayout.CENTER);
    this.add(exitPane, BorderLayout.SOUTH);
    data = new ImportData();
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    if (event.getSource() == importButton) {
      this.importAction();
    }
    if (event.getSource() == exportButton) {
      this.exportAction();
    }
    if (event.getSource() == exitButton) {
      this.dispose();
    }
  }

  private void exportAction() {
    if (data.getxArray() == null) {
      JOptionPane.showMessageDialog(this, "No import data", "No import data",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
    JFileChooser fileopen = new JFileChooser();
    fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileopen.setAcceptAllFileFilterUsed(false);
    int ret = fileopen.showDialog(this, "Choose folder");
    if (ret != JFileChooser.APPROVE_OPTION) {
      JOptionPane.showMessageDialog(this, "Folder wasn't choosen",
                                    "Folder wasn't choosen",
                                    JOptionPane.ERROR_MESSAGE);
      return;
    }
    Excel excel = new Excel();
    try {
      excel.ExportToFile(fileopen.getSelectedFile().getAbsolutePath(), data);
    } catch (WrongExcelFileException e) {
      JOptionPane.showMessageDialog(this, "Oooops: " + e.getMessage(), "Oooops",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  private void importAction() {
    JFileChooser fileopen = new JFileChooser();
    int ret = fileopen.showDialog(null, "Choose file");
    if (ret != JFileChooser.APPROVE_OPTION) {
      return;
    }
    Excel excel = new Excel();
    try {
      data = excel.ImportFromFile(fileopen.getSelectedFile().getAbsolutePath());
    } catch (WrongExcelFileException e) {
      JOptionPane.showMessageDialog(this, "Oooops: " + e.getMessage(), "Oooops",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }
}
