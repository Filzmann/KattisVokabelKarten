import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.util.Random;
//import java.util.ArrayList;

public class VokabelKarten {
    private JPanel panelContainer;
        private JPanel start;
            private JButton karteAnlegenButton;
            private JButton karteBearbeitenButton;
            private JButton lernenButton;
        private JPanel karteAnlegen;
            private JTextField englishWord;
            private JTextField germanWord;
            private JTextArea englDescr;
            private JButton cancel;
            private JButton saveAndBack;
            private JButton saveAndNew;
        private JPanel karteBearbeiten;
            private JScrollPane left;
                private JList wortListe;
                private vokabelListModel wortListeModel = new vokabelListModel();
                int actIndex;
            private JScrollPane right;
                int lastEditIndex;
                String wordToEdit;
                private JTextField editEnglishWord;
                private JTextField editGermanWord;
                private JTextArea editEnglDescr;
                private JButton speichernButton;
            private JButton abbruchButton;



        private JPanel Lernen;
    Eintrag aktEintragLernen;
    int englOrGerman;
    private JCheckBox checkEnglish;
    private JCheckBox checkGerman;
    private JTextField answer;
    private JTextField question;
    private JButton checkButton;
    private JButton zeigeBeschreibungButton;
    private JButton zeigeErstenBuchstabenButton;
    private JButton nächsteFrageButton;
    private JTextPane description;
    private JButton Zurück;
    private JButton lösungButton;


    CardLayout cl = new CardLayout();


    public VokabelKarten() {
        panelContainer.setLayout(cl);
        panelContainer.add(start, "start");
        panelContainer.add(karteAnlegen, "karteAnlegen");
        panelContainer.add(karteBearbeiten, "karteBearbeiten");
        panelContainer.add(Lernen, "Lernen");

        wortListe.setModel(wortListeModel);
        for(Eintrag row:Eintrag.eintraege) wortListeModel.addElement(row);

        //Startseite
        karteAnlegenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(panelContainer, "karteAnlegen");
            }
        });
        karteBearbeitenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(panelContainer, "karteBearbeiten");
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(panelContainer, "start");
            }
        });

        saveAndNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Eintrag eintrag = new Eintrag(englishWord.getText(), germanWord.getText(), englDescr.getText());
                saveCard(eintrag);
                cl.show(panelContainer, "karteAnlegen");
            }
        });
        saveAndBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Eintrag eintrag = new Eintrag(englishWord.getText(), germanWord.getText(), englDescr.getText());


                saveCard(eintrag);

                cl.show(panelContainer, "start");
            }
        });

        //edit Panel
        abbruchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    cl.show(panelContainer, "start");
            }
        });

       wortListe.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                actIndex = wortListe.getSelectedIndex();
                wordToEdit = (String) wortListe.getSelectedValue();
                Eintrag dataToEdit = wortListeModel.getFullElementAt(actIndex);
                editEnglishWord.setText(dataToEdit.getEnglishWord());
                editGermanWord.setText(dataToEdit.getGermanWord());
                editEnglDescr.setText(dataToEdit.getEnglishDescription());
                }

        });

        speichernButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //entferne unmodifizierten Datensatz
                Eintrag actEintrag = Eintrag.findByEnglishWord(wordToEdit);
                Eintrag abspeichern = new Eintrag(editEnglishWord.getText(), editGermanWord.getText(), editEnglDescr.getText());
                Eintrag.eintraege.remove(actEintrag);
                wortListeModel.set(actIndex, abspeichern);
                //füge neuen Datensatz hinzu
                saveCard(    abspeichern   );


            }
        });


        lernenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                neueAufgabe();
                cl.show(panelContainer, "Lernen");
            }
        });
        Zurück.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(panelContainer,"start");
            }
        });
        nächsteFrageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                neueAufgabe();
            }
        });
        zeigeErstenBuchstabenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String antwort;
                if(englOrGerman==0)
                    antwort= aktEintragLernen.getGermanWord();
                else antwort= aktEintragLernen.getEnglishWord();

                answer.setText(antwort.substring(0,1));
                answer.setBackground(SystemColor.window);
            }
        });
        zeigeBeschreibungButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                description.setText(aktEintragLernen.getEnglishDescription());
            }
        });
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean korrekt =false;
                String richtigeAntwort;
                if(englOrGerman==0) richtigeAntwort=aktEintragLernen.getGermanWord();
                else richtigeAntwort= aktEintragLernen.getEnglishWord();


                if(answer.getText().trim().equalsIgnoreCase(richtigeAntwort)) korrekt=true;
                System.out.println(answer.getText() + "==" + richtigeAntwort);

                if(korrekt)
                {
                    answer.setBackground(Color.green);
                }
                else
                    answer.setBackground(Color.red);
            }
        });
        lösungButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String antwort;
                if(englOrGerman==0)
                    antwort= aktEintragLernen.getGermanWord();
                else antwort= aktEintragLernen.getEnglishWord();

                answer.setText(antwort);
                answer.setBackground(SystemColor.window);
            }
        });
    }

    private void neueAufgabe() {

        aktEintragLernen = Eintrag.getRandomEintrag();
        if(checkEnglish.isSelected()&& checkGerman.isSelected())
        {
            Random rnd= new Random();
            englOrGerman = rnd.nextInt(2);
        }else if (checkGerman.isSelected()) englOrGerman=1;
        else {
            checkEnglish.setSelected(true);
            englOrGerman=0;
        }
        if(englOrGerman==0)
            question.setText(aktEintragLernen.getEnglishWord());
        else question.setText(aktEintragLernen.getGermanWord());


        //reset
        answer.setText("");

        answer.setBackground(SystemColor.window);
        description.setText("");

    }




    private void saveCard(Eintrag eintrag) {

        try {

            Eintrag.eintraege.add(eintrag);
            //TODO: Einträge vor dem Speichern alphabetisch sortieren
            wortListeModel.addElement(eintrag);
            FileWriter csvWriter = new FileWriter("data.csv");
            for (Eintrag zeile:
                Eintrag.eintraege) {
                csvWriter.append(zeile.getEnglishWord().trim());
                csvWriter.append("###");
                csvWriter.append(zeile.getGermanWord().trim());
                csvWriter.append("###");
                //TODO: mit Zeilenumbrüchen umgehen
                csvWriter.append(zeile.getEnglishDescription().replaceAll("\r", "").replaceAll("\n", "<br />").trim());
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        englishWord.setText("");
        germanWord.setText("");
        englDescr.setText("");
    }



    public static void main(String[] args) {

        Eintrag.readData("data.csv");
        JFrame frame = new JFrame("Alles");
        frame.setContentPane(new VokabelKarten().panelContainer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
