import javax.swing.DefaultListModel;

public class vokabelListModel extends DefaultListModel {
    public Object getElementAt(int index)
    {
        Eintrag eintrag = (Eintrag) super.getElementAt(index);
        return eintrag.getEnglishWord();
    }
    public Eintrag getFullElementAt(int index)
    {
        Eintrag eintrag = (Eintrag) super.getElementAt(index);
        return eintrag;
    }

}
