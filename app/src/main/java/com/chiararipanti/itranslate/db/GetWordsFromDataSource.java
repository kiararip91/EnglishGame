package com.chiararipanti.itranslate.db;

import com.chiararipanti.itranslate.model.Word;

import java.util.ArrayList;

/**
 * Created by chiararipanti on 04/02/18.
 */

public class GetWordsFromDataSource {
    private ArrayList<Word> words;

    public GetWordsFromDataSource() {
        words = new ArrayList<>();
    }

    /*public ArrayList<Word> getWords(int level){
        words = new ArrayList<>();

        Query wordsQuery = FirebaseDatabase.getInstance().getReference("Word").orderByChild("level").equalTo(level);
        wordsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                   Word word = child.getValue(Word.class);
                   words.add(word);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Logic for failure is here...
            }
        });

        return  words;
    }*/

}
