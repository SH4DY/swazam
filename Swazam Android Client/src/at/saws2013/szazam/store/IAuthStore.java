package at.saws2013.szazam.store;

public interface IAuthStore {
	
	String getToken();
	void setToken(String token);
	void invalidateToken();

}
