package com.alan.aeroplanechess;

import com.alan.aeroplanechess.service.NetworkService;
import com.alan.aeroplanechess.service.impl.NetworkServiceImpl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void networkConmunication() throws Exception{
        NetworkService networkService=new NetworkServiceImpl();
        networkService.setListener("0.0.0.0",Constants.NET_INVITATION,(ip, type, message) -> {
            assertEquals(message,"Hello");
        });
        networkService.send("0.0.0.0",Constants.NET_INVITATION,"Hello");
    }
}