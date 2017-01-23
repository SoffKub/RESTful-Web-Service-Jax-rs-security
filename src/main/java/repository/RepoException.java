package repository;

public final class RepoException extends Exception {

	private static final long serialVersionUID = -4873403939274419488L;

	public RepoException(String message) {
		super(message);
	}

	public RepoException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
