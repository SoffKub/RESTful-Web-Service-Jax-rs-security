package model;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
public final class User extends BaseEntity {

	private String firstname;
	private String lastname;
	private String status;

	@Column(unique = true)
	private String usernumber;

	@Column(unique = true)
	private String username;

	@Column(length = 1000)
	private String password;

	@Column(length = 1000)
	private String salt;

	@Column(length = 1000)
	private String token;

	private String expirationTime;

	@ManyToOne
	private Team team;

	public User() {
	}

	public User(String firstname, String lastname, String username, Team team) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.team = team;
		this.status = Status.ACTIVE.toString();
	}

	public User(String firstname, String lastname, String username, String password, Team team) {
		this(firstname, lastname, username, team);
		this.salt = generateSalt();
		this.password = hashPassword(password.toCharArray(), Base64.decodeBase64(salt));
	}

	private String generateSalt() {
		SecureRandom secureRandom;
		byte[] salt = new byte[64];
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.nextBytes(salt);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		Logger logger = Logger.getLogger("JUser");
		logger.log(Level.SEVERE, "Salt is " + Base64.encodeBase64String(salt));
		return Base64.encodeBase64String(salt);

	}

	public String hashPassword(char[] string, byte[] salt) {
		int iterations = 40000;
		PBEKeySpec spec = new PBEKeySpec(string, salt, iterations, 64 * 8);
		SecretKeyFactory skf;
		byte[] hashKey = new byte[64];
		try {
			skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hashKey = skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return Base64.encodeBase64String(hashKey);
	}

	public User generateUsernumber() {
		Random randomGenerator = new Random();
		this.usernumber = firstname.substring(0, 1) + lastname.substring(0, 1) + randomGenerator.nextInt(1000);
		return this;
	}

	public String getUsernumber() {
		return usernumber;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(Status userStatus) {
		this.status = userStatus.toString();
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPassword() {
		return password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}
}
