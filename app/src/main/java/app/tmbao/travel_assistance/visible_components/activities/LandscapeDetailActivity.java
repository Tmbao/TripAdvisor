package app.tmbao.travel_assistance.visible_components.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import app.tmbao.travel_assistance.R;
import app.tmbao.travel_assistance.invisible_components.models.Landscape;
import app.tmbao.travel_assistance.invisible_components.models.LandscapeResource;
import app.tmbao.travel_assistance.invisible_components.storage.LandscapeStorage;
import app.tmbao.travel_assistance.invisible_components.utilities.MediaHelpers;

public class LandscapeDetailActivity extends AppCompatActivity {

    private SliderLayout mDemoSlider;
    private int landscapeID;
    private List<LandscapeResource> landscapeResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape_detail);

        landscapeID = getIntent().getIntExtra("landscapeID", 0);

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        Landscape landscape = LandscapeStorage.getInstance().getLandscapes().get(landscapeID);
        List<LandscapeResource> imageResources = landscape.getImageResources();
        landscapeResources = landscape.getResources();

        for (int i = 0; i < imageResources.size(); ++i) {
            TextSliderView textSliderView = new TextSliderView(this);

            File imageFile = null;

            try {
                imageFile = MediaHelpers.getLandscapeResourceFile(imageResources.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            textSliderView
                    .description(landscape.getName())
                    .image(imageFile)
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

        TextView textView = (TextView) findViewById(R.id.tvDescription);
        textView.setText(landscape.getDescription());

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.icon_container);

        for (final LandscapeResource landscapeResource : landscapeResources) {
            if (landscapeResource.getType() == LandscapeResource.ResourceType.YOUTUBE) {
                ImageView youtubeIcon = new ImageView(this);
                final float scale = this.getResources().getDisplayMetrics().density;
                int pixels = (int) (50 * scale + 0.5f);
                youtubeIcon.setLayoutParams(new ViewGroup.LayoutParams(pixels, pixels));
                youtubeIcon.setImageResource(R.drawable.youtube_icon);
                youtubeIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(landscapeResource.getContent()));
                        startActivity(browserIntent);
                    }
                });
                linearLayout.addView(youtubeIcon);
            }
            else if (landscapeResource.getType() == LandscapeResource.ResourceType.WIKIPEDIA) {
                ImageView wikiIcon = new ImageView(this);
                final float scale = this.getResources().getDisplayMetrics().density;
                int pixels = (int) (50 * scale + 0.5f);
                wikiIcon.setLayoutParams(new ViewGroup.LayoutParams(pixels, pixels));
                wikiIcon.setImageResource(R.drawable.wiki_icon);
                wikiIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(landscapeResource.getContent()));
                        startActivity(browserIntent);
                    }
                });
                linearLayout.addView(wikiIcon);
            }
            else if (landscapeResource.getType() == LandscapeResource.ResourceType.WEBSITE) {
                ImageView websiteIcon = new ImageView(this);
                final float scale = this.getResources().getDisplayMetrics().density;
                int pixels = (int) (50 * scale + 0.5f);
                websiteIcon.setLayoutParams(new ViewGroup.LayoutParams(pixels, pixels));
                websiteIcon.setImageResource(R.drawable.website_icon);
                websiteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(landscapeResource.getContent()));
                        startActivity(browserIntent);
                    }
                });
                linearLayout.addView(websiteIcon);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landscape_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDirectionButtonClick(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("landscapeID", landscapeID);
        startActivity(intent);
    }
}
