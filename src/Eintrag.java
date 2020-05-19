import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Eintrag {
    static ArrayList<Eintrag> eintraege;
    private String englishWord;
    private String germanWord;
    private String englishDescription;
    private int rightAnswerCountG2E;
    private int rightAnswerCountE2G;

    public int getRightAnswerCountG2E() {
        return rightAnswerCountG2E;
    }

    public void setRightAnswerCountE2G(int rightAnswerCountG2E, String englishWord) {
        this.rightAnswerCountG2E = rightAnswerCountG2E;
    }

    public int getRightAnswerCountE2G() {
        return rightAnswerCountE2G;
    }

    public void setRightAnswerCountE2G(int rightAnswerCountE2G) {
        this.rightAnswerCountE2G = rightAnswerCountE2G;
    }



    private boolean checkFile(File file) {
        if (file != null) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating " + file.toString());
            }
            if (file.isFile() && file.canWrite() && file.canRead())
                return true;
        }
        return false;
    }

    public static void readData(String path)
    {
        File file= new File(path);
        if(!file.isFile())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating " + file.toString());
            }
            if (file.isFile() && file.canWrite() && file.canRead()) System.out.println(file.toPath() + "erzeugt");


        }


        String row;
        BufferedReader csvReader = null;
        try
        {
            csvReader = new BufferedReader(new FileReader(path));
            ArrayList<Eintrag> test = new ArrayList<>();
            while ((row = csvReader.readLine()) != null)
            {
                String[] data = row.split("###");
                test.add(
                        new Eintrag(
                                data[0],
                                data[1],
                                data[2].replaceAll("<br />", "\n"),
                                Integer.parseInt(data[3]),
                                Integer.parseInt(data[4])
                        )
                );
            }
            Eintrag.eintraege = test;
            csvReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void saveCard(Eintrag eintrag) {

        try {

            eintraege.add(eintrag);
            sortiereEintraege();
            VokabelKarten.wortListeModel.addElement(eintrag);
            FileWriter csvWriter = new FileWriter("data.csv");
            for (Eintrag zeile:
                    Eintrag.eintraege) {
                csvWriter.append(zeile.getEnglishWord().trim());
                csvWriter.append("###");
                csvWriter.append(zeile.getGermanWord().trim());
                csvWriter.append("###");
                csvWriter.append(zeile.getEnglishDescription().replaceAll("\r", "").replaceAll("\n", "<br />").trim());
                csvWriter.append("###");
                csvWriter.append(Integer.toString(zeile.getRightAnswerCountE2G()));
                csvWriter.append("###");
                csvWriter.append(Integer.toString(zeile.getRightAnswerCountG2E()));
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Eintrag(String englishWord, String germanWord, String englishDescription) {
        this.englishWord = englishWord;
        this.englishDescription = englishDescription;
        this.germanWord = germanWord;
    }

    public Eintrag(String englishWord, String germanWord, String englishDescription, int rightAnswerCountE2G, int rightAnswerCountG2E) {
        this.englishWord = englishWord;
        this.englishDescription = englishDescription;
        this.germanWord = germanWord;
        this.rightAnswerCountE2G = rightAnswerCountE2G;
        this.rightAnswerCountG2E=rightAnswerCountG2E;
    }


    public String getGermanWord() {
        return germanWord;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getEnglishDescription() {
        return englishDescription;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public void setGermanWord(String germanWord) {
        this.germanWord = germanWord;
    }

    public void setEnglishDescription(String englishDescription) {
        this.englishDescription = englishDescription;
    }

    public String toString() {
        return englishWord + " " + englishDescription + " " + germanWord;
    }

    public static Eintrag findByEnglishWord(String englishWord)
    {
        //TODO: Suche implementieren
        for (Eintrag zeile: eintraege) {
            if(zeile.englishWord.equalsIgnoreCase(englishWord)) {
                return zeile;
            }
        }
        return new Eintrag("n/a", "n/a", "n/a");
    }


    public static Eintrag getRandomEintrag() {
        //TODO: berücksichtige Lernerfolg

        Random random = new Random();
        Eintrag zufaelligerEintrag = eintraege.get(random.nextInt(eintraege.size()-1));
        return zufaelligerEintrag;
    }

    public static void sortiereEintraege()
    {
        ArrayList<Eintrag> ausgabeliste = new ArrayList<>();
        ArrayList<Eintrag> zwischenliste = new ArrayList<>();
        zwischenliste=eintraege;
        while(!zwischenliste.isEmpty())
        {
            Eintrag kleinster=kleinster(zwischenliste);
            ausgabeliste.add(kleinster);
            zwischenliste.remove(kleinster);
        }
        eintraege=ausgabeliste;
    }
    public static Eintrag min(Eintrag erster, Eintrag zweiter)
    {
        int vergleich = erster.getEnglishWord().compareToIgnoreCase(zweiter.getEnglishWord());
        if (vergleich < 0) return erster;
        else return zweiter;
    }
    public static Eintrag kleinster(ArrayList<Eintrag> eintraege)
    {
        if(eintraege.size()<2)
            return eintraege.get(0);
        Eintrag zwischenspeicher = eintraege.get(0);
        for(Eintrag zeile:eintraege)
        {
            zwischenspeicher=min(zwischenspeicher,zeile);
        }
        return zwischenspeicher;
    }

}
