import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Main {

    static int lengthOfPrime = 512;
    static int lengthOfRandom = 1_000_000;
    static BigInteger seed;
    static BigInteger n;
    static StringBuilder randomNumberBBS;
    static StringBuilder randomNumberJacobi;

    static void setup(){

        BigInteger parity;
        BigInteger p;
        BigInteger q;

        do {
            p = new BigInteger(lengthOfPrime, 1, new Random());
            parity = p.mod(BigInteger.valueOf(4));
        }while(!parity.toString().equals("3"));

        do {
            q = new BigInteger(lengthOfPrime, 1, new Random());
            parity = p.mod(BigInteger.valueOf(4));
        }while(!parity.toString().equals("3"));

        n = p.multiply(p);

        System.out.println("n = " + p + " * " + q + " = " + n );

        Date date = new Date();
        seed = BigInteger.valueOf( date.getTime() );
        seed = seed.multiply(seed);
        seed = seed.mod(n);

        System.out.println("The seed is: " + seed);
    }

    static void BBS(){

        randomNumberBBS = new StringBuilder();
        long numberOfOne  = 0;
        long numberOfZero = 0;

        for( long position = 0 ; position < lengthOfRandom ; position++ ){

            seed = seed.multiply(seed);
            seed = seed.mod(n);

            BigInteger parity = seed.mod(BigInteger.valueOf(2));
            randomNumberBBS.append(parity);

            if( parity.toString().equals("0"))
                numberOfZero++;
            else
                numberOfOne++;
        }

        System.out.println("Blum-Blum-Shub pseudo-random numbers is: ");
        //System.out.println(randomNumber);

        System.out.println("\nNumber of 0: " + numberOfZero + " | Number of 1: " + numberOfOne);

        List<Integer> compressed = LZW.encode(randomNumberBBS.toString());

        System.out.println("Compressed pseudo-random number length is: " + compressed.size() + " || Original pseudo-random number length is: " + randomNumberBBS.length());
        System.out.println("Compression ration is: " + (double)compressed.size()/lengthOfRandom);

        StringBuilder oneZero = new StringBuilder();
        for( int index = 0 ; index < 1_000_000 ; index++ )
            if( index % 2 == 0 )
                oneZero.append("1");
        else
            oneZero.append("0");

        List<Integer> compressedOneZero = LZW.encode(oneZero.toString());
        System.out.println("Compressed zero-one number length is: " + compressedOneZero.size() + " || Original zero-one number length is: " + oneZero.length());
        System.out.println("Compression ration is: " + (double)compressedOneZero.size()/lengthOfRandom);
    }

    static String jacobiSymbol(BigInteger a, BigInteger n){

        BigInteger b;
        b = a.mod(n);
        int s = 1;

        while( b.compareTo( BigInteger.valueOf(2)) >= 0 ){

            while( b.mod( BigInteger.valueOf(4) ).compareTo( BigInteger.valueOf(0) ) == 0  ){
                b = b.divide( BigInteger.valueOf(4) );
            }

            if( b.mod( BigInteger.valueOf(2) ).compareTo( BigInteger.valueOf(0) ) == 0 ){

                if( n.mod( BigInteger.valueOf(8) ).compareTo( BigInteger.valueOf(3) ) == 0 || n.mod( BigInteger.valueOf(8) ).compareTo( BigInteger.valueOf(5) ) == 0 )
                    s *= -1;

                b = b.divide( BigInteger.valueOf(2) );
            }

            if( b.compareTo( BigInteger.valueOf(1) ) == 0 ){
                break;
            }

            if( b.mod( BigInteger.valueOf(4) ).compareTo( BigInteger.valueOf(3) ) == 0 )
                if( n.mod( BigInteger.valueOf(4) ).compareTo( BigInteger.valueOf(3) ) == 0 )
                    s *= -1;

            BigInteger copyOfB = b;
            b = n.mod(b);
            n = copyOfB;
        }

        b = b.multiply( BigInteger.valueOf(s) );
        return b.toString();
    }

    static void jacobiPseudoRandom(){

        randomNumberJacobi = new StringBuilder();
        long numberOfOne  = 0;
        long numberOfZero = 0;

        for( long position = 0 ; position < 100 ; position++ ){

            String jacobi = jacobiSymbol( seed, n );
            //System.out.println(jacobi);
            if( jacobi.compareTo("1") == 0 ){
                randomNumberJacobi.append("1");
                numberOfOne++;
            }else{
                randomNumberJacobi.append("0");
                numberOfZero++;
            }

            seed = seed.add(BigInteger.valueOf(1));

        }

        //System.out.println("Jacobi pseudo-random number is:");
        System.out.println("\nNumber of 0: " + numberOfZero + " | Number of 1: " + numberOfOne);
    }

    public static void main(String[] args) {

        setup();
        //BBS();
        //jacobiPseudoRandom();

        BigInteger nr1 = new BigInteger("134256139287544233423179392268687970851788538273761494746768964886465967391092377001067138608285696495046053782388184521874750960269008328068841529295181707698825604871839270829570019710186824976858713250752730471419354137096373503960907452651547963489623759066542040096683459770016349373775733544376491487280");
        BigInteger nr2 = new BigInteger("134256139287544233423179392268687970851788538273761494746768964886465967391092377001067138608285696495046053782388184521874750960269008328068841529295181707698825604871839270829570019710186824976858713250752730471419354137096373503960907452651547963489623759066542040096683459770016349373775733544376491487281");
        nr1 = nr1.multiply(nr1);
        System.out.println( jacobiSymbol(nr1, nr2));
    }
}
