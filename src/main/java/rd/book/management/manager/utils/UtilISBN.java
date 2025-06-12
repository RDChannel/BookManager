package rd.book.management.manager.utils;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class UtilISBN {//A Util component used to create a ISBN number

    public String Generate(){
        Random random = new Random();
        int checkSum = 0;
        String start = "978";
        StringBuilder isbn = new StringBuilder(start);
        for(int i = 0; i < 9; i++){
            int randomNum = random.nextInt(10);
            isbn.append(randomNum);
            checkSum += (i % 2 == 0) ? randomNum * 1 : randomNum * 3;
        }
        checkSum = (10 - (checkSum % 10)) % 10;
        isbn.append(checkSum);
        return isbn.toString();
    }
}

