/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.persistence.codelab.step3;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.codelabs.persistence.R;
import com.example.android.persistence.codelab.db.AppDatabase;
import com.example.android.persistence.codelab.db.Book;
import com.example.android.persistence.codelab.db.Loan;
import com.example.android.persistence.codelab.db.User;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BooksBorrowedByUserActivity extends AppCompatActivity {

    private BooksBorrowedByUserViewModel mViewModel;

    @SuppressWarnings("unused")
    private TextView mBooksTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.db_activity);
        mBooksTextView = findViewById(R.id.books_tv);

        // Get a reference to the ViewModel for this screen.
        mViewModel = ViewModelProviders.of(this).get(BooksBorrowedByUserViewModel.class);

        // Update the UI whenever there's a change in the ViewModel's data.
        subscribeUiBooks();
    }

    public void onRefreshBtClicked(View view) {
        mViewModel.createDb();
    }

    public void onTestBtClicked(View view) {
        AppDatabase roomDB = AppDatabase.getInDiskDatabase(getApplicationContext());
        Book book = new Book();
        book.id = 214356;
        book.title = "TestTitle";
        roomDB.bookModel().insertBook(book);

        User user2 = new User();
        user2.name = "Mike";
        user2.lastName = "Saeveristo Tens cá disto??";
        user2.age = 25;
        user2.address = "Pafarrone";
        user2.id = 14;
        roomDB.userModel().insertUser(user2);

        Date today = getTodayPlusDays(0);
        Date yesterday = getTodayPlusDays(-1);

        Loan loan = new Loan();
        loan.bookId = book.id;
        loan.userId = user2.id;
        loan.startTime = yesterday;
        loan.endTime = today;
        roomDB.loanModel().insertLoan(loan);

    }

    private static Date getTodayPlusDays(int daysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, daysAgo);
        return calendar.getTime();
    }

    private void subscribeUiBooks() {
        // DONE: refresh the list of books when there's new data
        mViewModel.books.observe(this, new Observer<List<Book>>() {
            /** Called when the data is changed.
             ** @param books The new data */
            @Override
            public void onChanged(@Nullable List<Book> books) {
                showBooksInUi(books);
            }
        });
    }

    @SuppressWarnings("unused")
    private void showBooksInUi(final @NonNull List<Book> books) {
        StringBuilder sb = new StringBuilder();

        for (Book book : books) {
            sb.append(book.title);
            sb.append("\n");

        }
        mBooksTextView.setText(sb.toString());
    }
}
