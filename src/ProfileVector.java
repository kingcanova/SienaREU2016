public class ProfileVector
{
    private int dim = 0; // dimensions ie # of possible categories
    public final double magnitude; // vector magnitude (precalculated)
    protected int[] nonzero, // indexes of nonzero elems in coordinates
        coordinates = new int[dim]; // coordinates of vector in dim dimensional space

    /**
     * slow
     */
    public ProfileVector(int[] coor) {
        if (coor.length == this.dim)
            for (int i=0; i<coor.length; i++)
                coordinates[i] = coor[i];
        else
            System.err.printf("Profile vector needs to be init w/ %d dimensions, was init w/ %d",
                              this.dim, coor.length);

            
        // initialize magnitude
        double total = 0;
        for (int i : this.coordinates)
            total += i*i;
        magnitude = Math.sqrt(total);
    }

    /**
     * fast
     * @param nonz the indexes of the nonzero elements in coor
     */
    public ProfileVector(int[] coor, int[] nonz) {
        if (coor.length == this.dim)
            for (int i=0; i<coor.length; i++)
                coordinates[i] = coor[i];
        else
            System.err.printf("Profile vector needs to be init w/ %d dimensions, was init w/ %d",
                              this.dim, coor.length);

        // copy nonz into nonzero
        nonzero = new int[nonz.length];
        for (int i=0; i<nonz.length; i++)
            nonzero[i] = nonz[i];
         
        // initialize magnitude
        double total = 0;
        for (int i : nonzero)
            total += coor[i] * coor[i];
        magnitude = Math.sqrt(total);
    }

    // turn a profile into a vector
    public static ProfileVector vectorize(Profile pro) {
        // need to map a category to a unique integer b/t 0 and # of categories
        // so that it can correspond to the index in the vector
        return null;
    }

    // I suppose we would need something like this too
    public static ProfileVector vectorize(Suggestion sug) {
        return null;
    }

    // dot product
    private double dot(ProfileVector argvec) {
        double tot = 0;
        for (int i=0; i<this.dim; i++)
            tot += this.coordinates[i] * argvec.coordinates[i];
        return tot;
    }

    // dot product optomized
    private double dotOptimized(ProfileVector argvec) {
        double tot = 0;
        for (int i : nonzero)
            tot += this.coordinates[i] * argvec.coordinates[i];
        return tot;
    }
    
    // use the precalculated value instead of this to save time
    private double magnitude() {
        double tot = 0;
        for (int i : this.coordinates)
            tot += i*i;
        return Math.sqrt(tot);            
    }

    // make dot know which ones are non-zero so it runs faster
    public double cosineSim(ProfileVector argvec) {
        return this.dotOptimized(argvec) / (this.magnitude * argvec.magnitude);
    }
}
