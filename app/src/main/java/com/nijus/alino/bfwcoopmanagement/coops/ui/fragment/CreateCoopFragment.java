package com.nijus.alino.bfwcoopmanagement.coops.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.adapter.CreateCoopStepper;
import com.nijus.alino.bfwcoopmanagement.coops.sync.AddCoop;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.AbstractWizardModel;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.ModelCallbacks;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.AccessToInformation;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.AvailableResources;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineFinanceInfo;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineSales;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineYield;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.ForecastSales;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.GeneralInformation;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.InternalInformation;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.PageFragmentCallbacks;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.StepPagerStrip_coop;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventB;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventR;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateCoopFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateCoopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCoopFragment extends Fragment implements ModelCallbacks,
        View.OnClickListener, ViewPager.OnPageChangeListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String LOG_TAG = CreateCoopFragment.class.getSimpleName();
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private AbstractWizardModel mCoopWizard;
    private Bundle saveBundle = new Bundle();
    private ProgressDialog progressDialog = new ProgressDialog();

    private Button mNextButton;
    private Button mPrevButton;
    private Button mSaveFarmer;
    private ImageView mImageView;
    private ImageView mRightImageView;

    private List<Page> mCurrentPageSequence;
    private final Pattern phoneRegex = Pattern.compile("^\\+250[0-9]{9}$",
            Pattern.CASE_INSENSITIVE);
    private StepPagerStrip_coop mStepPagerStripCoop;
    private CreateCoopStepper mPageAdapter;
    private PageFragmentCallbacks mCallbacks;
    private ViewPager pager;

    public CreateCoopFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateVendorFragment.
     */
    public static CreateCoopFragment newInstance(String param1, String param2) {
        CreateCoopFragment fragment = new CreateCoopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mCoopWizard = mCallbacks.onGetFarmerWizard();

        if (savedInstanceState != null) {
            mCoopWizard.load(savedInstanceState.getBundle("model"));
        }

        mCoopWizard.registerListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.coop_wizard, container, false);
        pager = rootView.findViewById(R.id.pager_create_coop);

        mCurrentPageSequence = mCoopWizard.getCurrentPageSequence();

        mPageAdapter = new CreateCoopStepper(getActivity().getSupportFragmentManager(),
                getActivity(), mCurrentPageSequence);
        if (pager != null) pager.setAdapter(mPageAdapter);

        mStepPagerStripCoop = rootView.findViewById(R.id.strip);

        pager.addOnPageChangeListener(this);

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        mStepPagerStripCoop.setOnPageSelectedListener(new StepPagerStrip_coop.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected2(int position) {
                position = Math.min(mPageAdapter.getCount() - 1, position);
                if (pager.getCurrentItem() != position) {
                    pager.setCurrentItem(position);
                }
            }
        });

        mNextButton = rootView.findViewById(R.id.next_button);
        mPrevButton = rootView.findViewById(R.id.prev_button);
        mSaveFarmer = rootView.findViewById(R.id.save_button);

        mImageView = rootView.findViewById(R.id.left_icon);
        mImageView.setImageResource(R.drawable.ic_chevron_left_black_24dp);
        mRightImageView = rootView.findViewById(R.id.right_icon);
        mRightImageView.setImageResource(R.drawable.ic_chevron_right_black_24dp);

        mNextButton.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);
        mSaveFarmer.setOnClickListener(this);

        onPageTreeChanged();
        updateBottomBar();
        return rootView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mStepPagerStripCoop.setCurrentPage(position);
        updateBottomBar();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.next_button) {
            if (pager.getCurrentItem() == mCurrentPageSequence.size() - 1) {
                saveNewCoop();
            } else {
                if (pager.getCurrentItem() == 0) {
                    EventBus.getDefault().post(new DataValidEventB());
                } else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
            }
            updateBottomBar();
        } else if (view.getId() == R.id.prev_button) {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
            updateBottomBar();
        } else if (view.getId() == R.id.save_button) {
            saveNewCoop();
        }

    }

    public boolean validatePhone(String phone) {
        Matcher match = phoneRegex.matcher(phone);
        return match.find();
    }

    public void saveNewCoop() {
        //Get each page data
        for (Page page : mCurrentPageSequence) {
            Bundle data = page.getData();

            boolean isData = data.containsKey("general");
            boolean isInternal = data.containsKey("internalInformation");
            boolean isAvailableRes = data.containsKey("availableResources");


            boolean isAccessInfo = data.containsKey("accessToInformation");
            boolean isForecastSales = data.containsKey("forecast_sales");
            boolean isBaselineYield = data.containsKey("baselines_yield");
            boolean isBaselineSales = data.containsKey("baseline_sales");
            boolean isBaselineFin = data.containsKey("baseline_finance_info");

            if (isData) {
                GeneralInformation general = data.getParcelable("general");
                if (general != null) {
                    String phoneNumber = general.getPhone();
                    if (general.getName() == null || general.getAddress() == null || !validatePhone(phoneNumber)) {
                        Toast.makeText(getContext(), getResources().getString(R.string.data_valid_error_coop), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                saveBundle.putParcelable("general", general);
            }

            if (isInternal) {
                InternalInformation internalInformation = data.getParcelable("internalInformation");
                saveBundle.putParcelable("internalInformation", internalInformation);
            }
            if (isAvailableRes) {
                AvailableResources availableResources = data.getParcelable("availableResources");
                saveBundle.putParcelable("availableResources", availableResources);
            }


            if (isAccessInfo) {
                HashMap<String, AccessToInformation> accessToInformation = (HashMap<String, AccessToInformation>) data.getSerializable("accessToInformation");
                saveBundle.putSerializable("accessToInformation", accessToInformation);
            }
            if (isForecastSales) {
                HashMap<String, ForecastSales> forecastSales = (HashMap<String, ForecastSales>) data.getSerializable("forecast_sales");
                saveBundle.putSerializable("forecast_sales", forecastSales);
            }
            if (isBaselineYield) {
                HashMap<String, BaselineYield> baselineYield = (HashMap<String, BaselineYield>) data.getSerializable("baselines_yield");
                saveBundle.putSerializable("baselines_yield", baselineYield);
            }
            if (isBaselineSales) {
                HashMap<String, BaselineSales> baselineSales = (HashMap<String, BaselineSales>) data.getSerializable("baseline_sales");
                saveBundle.putSerializable("baseline_sales", baselineSales);
            }
            if (isBaselineFin) {
                HashMap<String, BaselineFinanceInfo> baselineFinanceInfo = (HashMap<String, BaselineFinanceInfo>) data.getSerializable("baseline_finance_info");
                saveBundle.putSerializable("baseline_finance_info", baselineFinanceInfo);
            }
        }
        Intent intent = new Intent(getContext(), AddCoop.class);
        intent.putExtra("coop_data", saveBundle);
        progressDialog.show(getFragmentManager(), "coopTag");
        getContext().startService(intent);
    }

    @Subscribe
    public void onDataValidEventR(DataValidEventR eventR) {
        if (eventR.isDataValid()) {
            pager.setCurrentItem(pager.getCurrentItem() + 1);
        }
    }

    @Subscribe
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        progressDialog.dismiss();
        getActivity().finish();
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPageAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mCoopWizard.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStripCoop.setPageCount(mCurrentPageSequence.size());
        mPageAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mCoopWizard.save());
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size();
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPageAdapter.getCutOffPage() != cutOffPage) {
            mPageAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void updateBottomBar() {
        int position = pager.getCurrentItem();
        if (position == mCurrentPageSequence.size() - 1) {
            mNextButton.setText(R.string.finish);
        } else {
            mNextButton.setText(R.string.next);
            TypedValue v = new TypedValue();
            getActivity().getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
            mNextButton.setEnabled(position != mPageAdapter.getCutOffPage());
        }

        mSaveFarmer.setVisibility(position > 0 && position < mCurrentPageSequence.size() - 1 ? View.VISIBLE : View.INVISIBLE);

        mPrevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
        mImageView.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCoopWizard.unregisterListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mCallbacks = (PageFragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
