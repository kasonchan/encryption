package encryption

/**
 * Created by kasonchan on 1/24/15.
 */
case class ED(e: Int, d: Int)

case class key(x: Int, n: Int)

trait RSA {
  /**
   * Returns the greatest common divisor of a and b *
   * @param a Integer
   * @param b Integer
   * @return gcd(a, b) Integer
   */
  def gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

  /**
   * Returns a random integer e such that the gcd(phi, e) = 1 *
   * @param phi Integer
   * @return e Integer
   */
  def pickE(phi: Int): Int = {
    val es = for (i <- 1 to phi if (gcd(i, phi) == 1)) yield i

    val g = scala.util.Random

    val r = g.nextInt(es.length - 1)

    es(r + 1)
  }

  /**
   * Returns a pair of e and d such that 1 < d < phi, and ed = 1 (mod phi) *
   * and e != d *
   * @param e Integer
   * @param phi Integer
   * @return ED(e: Int, d: Int)
   */
  def pickED(e: Int, phi: Int): ED = {
    val ds = for (i <- 1 to 1000) yield ((i * phi) + 1)

    val rs = for (i <- ds if ((i % e) == 0)) yield i

    val fs = rs.map(i => i / e)

    if (fs(0) == e) pickED(pickE(phi), phi)
    else ED(e, fs(0))
  }

  /**
   * Returns a pair of private and public keys using p = 13, q = 17 *
   * @return (key(e, n), key(d, n))
   */
  def generateKeyPair() = {
    val p = 13 // 7907
    val q = 17 // 7919

    val n = p * q
    println("n: " + n)

    val phi = (p - 1) * (q - 1)
    println("phi: " + phi)

    val e = pickE(phi)

    val ed = pickED(e, phi)
    println("e: " + ed.e + " " + "d: " + ed.d)

    (key(ed.e, n), key(ed.d, n))
  }

  /**
   * Encrypts the message m with key (e, n) and returns the encrypted message *
   * @param e Integer
   * @param n Integer
   * @param m String
   * @return encryptedMsg List[BigInt]
   */
  def encrypt(e: Int, n: Int, m: String): String = {
    val msg = stringToBigInt(m)

    val encryptedMsg = msg.map(m => (m.pow(e)) % n)

    bigIntToString(encryptedMsg)
  }

  /**
   * Decrypts the message m with the key (d, n) and returns the decrypted
   * message *
   * @param d Integer
   * @param n Integer
   * @param m List[BigInt]
   * @return decryptedMsg List[BigInt]
   */
  def decrypt(d: Int, n: Int, m: String): String = {
    val msg = stringToBigInt(m)

    val decryptedMsg = msg.map(m => (m.pow(d)) % n)

    bigIntToString(decryptedMsg)
  }

  /**
   * Converts a string m to a list of BigInt *
   * @param m String
   * @return List[BigInt]
   */
  def stringToBigInt(m: String): List[BigInt] = {
    m.map(m => BigInt(m)).toList
  }

  /**
   * Convert a list of BigInt to String*
   * @param l List[BigInt]
   * @return String
   */
  def bigIntToString(l: List[BigInt]): String = {
    (l.map(m => m.toChar)).mkString
  }
}