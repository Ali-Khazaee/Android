package co.biogram.socket;

public interface FutureCallback<V> {
    void onSuccess(V result);

    void onFailure(Throwable failure);
}