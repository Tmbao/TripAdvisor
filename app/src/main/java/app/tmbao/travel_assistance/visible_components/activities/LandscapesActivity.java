package app.tmbao.travel_assistance.visible_components.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import app.tmbao.travel_assistance.R;
import app.tmbao.travel_assistance.invisible_components.models.Landscape;
import app.tmbao.travel_assistance.invisible_components.storage.LandscapeStorage;

public class LandscapesActivity extends AppCompatActivity {

    public class LandscapeAdapter extends RecyclerView.Adapter<LandscapeAdapter.LandscapeViewHolder> implements Filterable {

        public List<Landscape> landscapeList;
        private List<Landscape> allLandscapes;

        public LandscapeAdapter(List<Landscape> landscapeList) {
            this.landscapeList = landscapeList;
            allLandscapes = new ArrayList<>(landscapeList);
        }


        @Override
        public int getItemCount() {
            return landscapeList.size();
        }

        @Override
        public void onBindViewHolder(LandscapeViewHolder landscapeViewHolder, int i) {
            Landscape landscape = landscapeList.get(i);
            landscapeViewHolder.vName.setText(landscape.getName());
            landscapeViewHolder.vDescription.setText(landscape.getDescription().substring(0, 50) + "...");
            try {
                Bitmap photo = landscape.getRepresentativePhoto();
                landscapeViewHolder.vPhoto.setImageBitmap(photo);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        }

        @Override
        public LandscapeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_layout, viewGroup, false);

            return new LandscapeViewHolder(itemView, i);
        }

        @Override
        public Filter getFilter() {
            return new LandscapeFilter(this, allLandscapes);
        }

        public class LandscapeViewHolder extends RecyclerView.ViewHolder {

            protected TextView vName;
            protected TextView vDescription;
            protected ImageView vPhoto;

            public LandscapeViewHolder(View v, int i) {
                super(v);
                vName =  (TextView) v.findViewById(R.id.tvName);
                vDescription = (TextView) v.findViewById(R.id.tvDescription);
                vPhoto = (ImageView) v.findViewById(R.id.ivPhoto);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int landscapeID = recyclerView.getChildAdapterPosition(view);
                        Intent intent = new Intent(LandscapesActivity.this, LandscapeDetailActivity.class);
                        intent.putExtra("landscapeID", landscapeID);
                        startActivity(intent);
                    }
                });
            }

        }
    }

    public class LandscapeFilter extends Filter {
        private final LandscapeAdapter mAdapter;
        List<Landscape> allLandscapes;
        List<Landscape> filteredList;

        public LandscapeFilter(LandscapeAdapter mAdapter, List<Landscape> allLandscapes) {
            this.mAdapter = mAdapter;
            this.allLandscapes = allLandscapes;
            filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            StringTokenizer stringTokenizer = new StringTokenizer(charSequence.toString(), " !.,?:;'\"()[]{}\\@#$%^&*`~");
            List<String> tokens = new ArrayList<>();
            while (stringTokenizer.hasMoreTokens())
                tokens.add(stringTokenizer.nextToken().toLowerCase());

            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (tokens.size() == 0) {
                filteredList.addAll(allLandscapes);
            }
            else {
                for (final Landscape landscape : allLandscapes) {
                    int countMatch = 0;
                    for (final String token : tokens) {
                        if (landscape.getName().toLowerCase().contains(token))
                            ++countMatch;
                        else if (landscape.getDescription().toLowerCase().contains(token))
                            ++countMatch;
                    }
                    if (countMatch == tokens.size())
                        filteredList.add(landscape);
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
            mAdapter.landscapeList.clear();
            mAdapter.landscapeList.addAll((ArrayList<Landscape>) filterResults.values);
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<Landscape> landscapeList = LandscapeStorage.getInstance().getLandscapes();
    private RecyclerView recyclerView;
    private LandscapeAdapter landscapeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscapes);
        initializeComponents();
    }

    private void initializeComponents() {
        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        landscapeAdapter = new LandscapeAdapter(landscapeList);
        recyclerView.setAdapter(landscapeAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landscapes, menu);

        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                landscapeAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.visual_search) {
            Intent intent = new Intent(this, VisualSearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
