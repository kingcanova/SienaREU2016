
/**
 * Write a description of class Temp here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Temp implements Comparable<Temp>
{
    protected int num_slash;
    protected int num_words;
    protected int len_title;
    protected int len_url;
    protected String line;
    
    public Temp(String l, int url, int slash, int words, int title)
    {
        line = l;
        len_url = url;
        num_slash = slash;
        num_words = words;
        len_title = title;
    }
    public int compareTo(Temp other)
    {
        if(this.num_words < other.num_words)
        {
            return -1;
        }
        else if (this.num_words > other.num_words)
        {
            return 1;
        }
        return 0;
    }
    public boolean equals(Temp other)
    {
        if(this.num_words == other.num_words)
        {
            return true;
        }
        return false;
    }
    public String toString()
    {
        return (line + "," + len_url + "," + num_slash + "," + num_words + "," + len_title + "\n"); 
    }
}
