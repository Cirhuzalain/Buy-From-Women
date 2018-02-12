package com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment;

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
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventB;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventR;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.ProgressDialog;
import com.nijus.alino.bfwcoopmanagement.vendors.adapter.CreateVendorStepper;
import com.nijus.alino.bfwcoopmanagement.vendors.sync.UpdateVendorService;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.AbstractWizardModelVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.ModelCallbacksVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.BaseLineVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.DemographicVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.FinanceVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.ForecastVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.GeneralVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.ServiceAccessVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.PageFragmentCallbacksVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.StepPagerStripVendor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

public class UpdateVendorFragment extends Fragment implements ModelCallbacksVendor,
        View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private AbstractWizardModelVendorVendor mFarmerWizard;
    private Bundle saveBundle = new Bundle();
    private ProgressDialog progressDialog = new ProgressDialog();

    private Button mNextButton;
    private Button mPrevButton;
    private Button mSaveFarmer;
    private ImageView mImageView;
    private ImageView mRightImageView;

    private List<PageVendorVendor> mCurrentPageVendorSequence;
    private StepPagerStripVendor mStepPagerStripVendor;
    private CreateVendorStepper mPageAdapter;
    private PageFragmentCallbacksVendor mCallbacks;
    private ViewPager pager;
    private long mFarmerId;

    public UpdateVendorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mFarmerWizard = mCallbacks.onGetUpdateWizard();

        if (savedInstanceState != null) {
            mFarmerWizard.load(savedInstanceState.getBundle("model"));
        }

        mFarmerWizard.registerListener(this);

        //get farmer Id
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("farmerId")) {
            mFarmerId = intent.getLongExtra("farmerId", 0);
        }

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
        View rootView = inflater.inflate(R.layout.update_wizard_vendor, container, false);
        pager = rootView.findViewById(R.id.pager_create_vendor_update);

        mCurrentPageVendorSequence = mFarmerWizard.getCurrentPageSequence();

        mPageAdapter = new CreateVendorStepper(getActivity().getSupportFragmentManager(),
                getActivity(), mCurrentPageVendorSequence);
        if (pager != null) pager.setAdapter(mPageAdapter);

        mStepPagerStripVendor = rootView.findViewById(R.id.strip_update_vendor);

        pager.addOnPageChangeListener(this);

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        mStepPagerStripVendor.setOnPageSelectedListener(new StepPagerStripVendor.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(mPageAdapter.getCount() - 1, position);
                if (pager.getCurrentItem() != position) {
                    pager.setCurrentItem(position);
                }
            }
        });

        mNextButton = rootView.findViewById(R.id.next_button_update);
        mPrevButton = rootView.findViewById(R.id.prev_button_update);
        mSaveFarmer = rootView.findViewById(R.id.save_button_update);
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
        mStepPagerStripVendor.setCurrentPage(position);
        updateBottomBar();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.next_button_update) {
            if (pager.getCurrentItem() == mCurrentPageVendorSequence.size() - 1) {
                //Update Farmer data
                updateFarmer();

            } else {
                if (pager.getCurrentItem() == 0) {
                    EventBus.getDefault().post(new DataValidEventB());
                } else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
            }
            updateBottomBar();
        } else if (view.getId() == R.id.prev_button_update) {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
            updateBottomBar();
        } else if (view.getId() == R.id.save_button_update) {
            //Update farmer data
            updateFarmer();
        }

    }

    public void updateFarmer() {
        //Get each page data
        //check if first page is filled if not hide progress dialog and show error message
        for (PageVendorVendor pageVendor : mCurrentPageVendorSequence) {
            Bundle data = pageVendor.getData();

            boolean isData = data.containsKey("general");
            boolean isLandInfo = data.containsKey("land_info");
            boolean isForecast = data.containsKey("forecast");
            boolean isDemographic = data.containsKey("demographic");
            boolean isBaseline = data.containsKey("baseline");
            boolean isFinance = data.containsKey("finance");
            boolean isService = data.containsKey("serviceAccess");

            if (isData) {
                GeneralVendor generalVendor = data.getParcelable("generalVendor");
                if (generalVendor != null) {
                    if (generalVendor.getName() == null || generalVendor.getPhoneNumber() == null) {
                        Toast.makeText(getContext(), getResources().getString(R.string.data_valid_error), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                saveBundle.putParcelable("generalVendor", generalVendor);
            }

            if (isLandInfo) {
                HashMap map = (HashMap) data.getSerializable("land_info");
                saveBundle.putSerializable("land_info", map);
            }

            if (isForecast) {
                ForecastVendor forecastVendor = data.getParcelable("forecastVendor");
                saveBundle.putParcelable("forecastVendor", forecastVendor);
            }
            if (isDemographic) {
                DemographicVendor demographicVendor = data.getParcelable("demographicVendor");
                saveBundle.putParcelable("demographicVendor", demographicVendor);
            }
            if (isBaseline) {
                BaseLineVendor baseLineVendor = data.getParcelable("baseline");
                saveBundle.putParcelable("baseline", baseLineVendor);
            }
            if (isFinance) {
                FinanceVendor financeVendor = data.getParcelable("financeVendor");
                saveBundle.putParcelable("financeVendor", financeVendor);
            }
            if (isService) {
                ServiceAccessVendor serviceAccessVendor = data.getParcelable("serviceAccessVendor");
                saveBundle.putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        }
        Intent intent = new Intent(getContext(), UpdateVendorService.class);
        intent.putExtra("farmer_data", saveBundle);
        intent.putExtra("farmerId", mFarmerId);
        progressDialog.show(getFragmentManager(), "farmerTag");
        getContext().startService(intent);
    }

    @Subscribe
    public void onDataValidEventR(DataValidEventR eventR) {
        if (eventR.isDataValid()) {
            pager.setCurrentItem(pager.getCurrentItem() + 1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        progressDialog.dismiss();
        Toast.makeText(getContext(), getResources().getString(R.string.update_msg), Toast.LENGTH_SHORT).show();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateVendorFragment.
     */
    public static UpdateVendorFragment newInstance(String param1, String param2) {
        UpdateVendorFragment fragment = new UpdateVendorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPageDataChanged(PageVendorVendor pageVendor) {
        if (pageVendor.isRequired()) {
            if (recalculateCutOffPage()) {
                mPageAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageVendorSequence = mFarmerWizard.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStripVendor.setPageCount(mCurrentPageVendorSequence.size());
        mPageAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mFarmerWizard.save());
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageVendorSequence.size();
        for (int i = 0; i < mCurrentPageVendorSequence.size(); i++) {
            PageVendorVendor pageVendor = mCurrentPageVendorSequence.get(i);
            if (pageVendor.isRequired() && !pageVendor.isCompleted()) {
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
        if (position == mCurrentPageVendorSequence.size() - 1) {
            mNextButton.setText(R.string.finish);
        } else {
            mNextButton.setText(R.string.next);
            TypedValue v = new TypedValue();
            getActivity().getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
            mNextButton.setEnabled(position != mPageAdapter.getCutOffPage());
        }

        mSaveFarmer.setVisibility(position > 0 && position < mCurrentPageVendorSequence.size() - 1 ? View.VISIBLE : View.INVISIBLE);

        mPrevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
        mImageView.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFarmerWizard.unregisterListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mCallbacks = (PageFragmentCallbacksVendor) context;
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
