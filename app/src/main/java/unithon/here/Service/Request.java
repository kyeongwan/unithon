package unithon.here.Service;

/**
 * Created by lk on 16. 3. 22..
 */
public interface Request {
    void onSuccess(String receiveData);
    void onFail(String url, String error);
}