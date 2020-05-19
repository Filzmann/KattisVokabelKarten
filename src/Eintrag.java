import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Eintrag {
    static ArrayList<Eintrag> eintraege;
    private String englishWord;
    private String germanWord;
    private String englishDescription;

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
                test.add(new Eintrag(data[0], data[1], data[2].replaceAll("<br />", "\n")));
            }
            Eintrag.eintraege = test;
            csvReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public Eintrag(String englishWord, String germanWord, String englishDescription) {
        this.englishWord = englishWord;
        this.englishDescription = englishDescription;
        this.germanWord = germanWord;
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
}
