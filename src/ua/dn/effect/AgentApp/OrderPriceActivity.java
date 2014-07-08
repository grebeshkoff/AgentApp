package ua.dn.effect.AgentApp;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import ua.dn.effect.AgentApp.data.model.Client;
import ua.dn.effect.AgentApp.data.model.EntityGroup;
import ua.dn.effect.AgentApp.data.model.Order;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: igrebeshkov
 * Date: 24.09.13
 * Time: 13:48
 */
public class OrderPriceActivity extends Activity{

    ListView lvProducts;
    EditText etSearch;
    ArrayAdapter<EntityGroup> adapter;
    ArrayList<HashMap<String, String>> productsList;
    Activity activity = this;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_price);

        lvProducts = (ListView) findViewById(R.id.list_view);
        etSearch = (EditText) findViewById(R.id.inputSearch);

        adapter = new ArrayAdapter<EntityGroup>(this, R.layout.order_price_group_item, R.id.product_name, AgentApplication.priceList.list);

        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

                EntityGroup group = (EntityGroup) parent.getAdapter().getItem(position);
                AgentApplication.currentOrder.currentGroup = group;

                Intent intent = new Intent(activity, OrderPriceGroupEntryActivity.class);

                View v = OrderPriceActivityGroup.group.getLocalActivityManager()
                        .startActivity("tag2", intent
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        .getDecorView();

                OrderPriceActivityGroup.group.replaceView(v);
            }
        });
        lvProducts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        lvProducts.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                OrderPriceActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        getParent().onBackPressed();
    }

//    @Override
//    public void onPause()
//    {
//        super.onPause();
//        finish();
//    }
}