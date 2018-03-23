package com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment;

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
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.CreateFarmerStepper;
import com.nijus.alino.bfwcoopmanagement.farmers.sync.UpdateFarmerService;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.AbstractWizardModel;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.ModelCallbacks;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.AccessToInformation;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.BaseLine;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Demographic;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Finance;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Forecast;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.General;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.LandInformation;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.ServiceAccess;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.PageFragmentCallbacks;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.StepPagerStrip;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateFarmerFragment extends Fragment implements ModelCallbacks,
        View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private AbstractWizardModel mFarmerWizard;
    private Bundle saveBundle = new Bundle();
    private ProgressDialog progressDialog = new ProgressDialog();

    private Button mNextButton;
    private Button mPrevButton;
    private Button mSaveFarmer;
    private ImageView mImageView;
    private ImageView mRightImageView;
    private final Pattern phoneRegex = Pattern.compile("^\\+250[0-9]{9}$",
            Pattern.CASE_INSENSITIVE);

    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;
    private CreateFarmerStepper mPageAdapter;
    private PageFragmentCallbacks mCallbacks;
    private ViewPager pager;
    private int mFarmerId;

    public UpdateFarmerFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateVendorFragment.
     */
    public static UpdateFarmerFragment newInstance(String param1, String param2) {
        UpdateFarmerFragment fragment = new UpdateFarmerFragment();
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

        mFarmerWizard = mCallbacks.onGetUpdateWizard();

        if (savedInstanceState != null) {
            mFarmerWizard.load(savedInstanceState.getBundle("model"));
        }

        mFarmerWizard.registerListener(this);

        //get farmer Id
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("farmerId")) {
            mFarmerId = intent.getIntExtra("farmerId", 0);
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
        View rootView = inflater.inflate(R.layout.update_wizard, container, false);
        pager = rootView.findViewById(R.id.pager_create_farmer_update);

        mCurrentPageSequence = mFarmerWizard.getCurrentPageSequence();

        mPageAdapter = new CreateFarmerStepper(getActivity().getSupportFragmentManager(),
                getActivity(), mCurrentPageSequence);
        if (pager != null) pager.setAdapter(mPageAdapter);

        mStepPagerStrip = rootView.findViewById(R.id.strip_update);

        pager.addOnPageChangeListener(this);

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        mStepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
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
        mStepPagerStrip.setCurrentPage(position);
        updateBottomBar();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public boolean validatePhone(String phone) {
        Matcher match = phoneRegex.matcher(phone);
        return match.find();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.next_button_update) {
            if (pager.getCurrentItem() == mCurrentPageSequence.size() - 1) {
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
            updateFarmer();
        }
    }

    public void updateFarmer() {
        //Get each page data
        for (Page page : mCurrentPageSequence) {
            Bundle data = page.getData();

            boolean isData = data.containsKey("general");
            boolean isDemographic = data.containsKey("demographic");
            boolean isService = data.containsKey("serviceAccess");

            boolean isForecast = data.containsKey("forecast");
            boolean isBaseline = data.containsKey("baseline");
            boolean isFinance = data.containsKey("finance");
            boolean isAccessToInformation = data.containsKey("accessToInformation");
            boolean isLandInfo = data.containsKey("land_info");

            if (isData) {
                General general = data.getParcelable("general");
                if (general != null) {
                    String phoneNumber = general.getPhoneNumber();
                    if (general.getName() == null || phoneNumber == null || !validatePhone(phoneNumber)) {
                        Toast.makeText(getContext(), getResources().getString(R.string.data_valid_error), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                saveBundle.putParcelable("general", general);
            }
            if (isDemographic) {
                Demographic demographic = data.getParcelable("demographic");
                saveBundle.putParcelable("demographic", demographic);
            }
            if (isService) {
                ServiceAccess serviceAccess = data.getParcelable("serviceAccess");
                saveBundle.putParcelable("serviceAccess", serviceAccess);
            }

            if (isLandInfo) {
                HashMap<String, LandInformation> landInfo = (HashMap<String, LandInformation>) data.getSerializable("land_info");
                saveBundle.putSerializable("land_info", landInfo);
            }

            if (isForecast) {
                HashMap<String, Forecast> forecast = (HashMap<String, Forecast>) data.getSerializable("forecast");
                saveBundle.putSerializable("forecast", forecast);

            }
            if (isBaseline) {
                HashMap<String, BaseLine> baseline = (HashMap<String, BaseLine>) data.getSerializable("baseline");
                saveBundle.putSerializable("baseline", baseline);

            }
            if (isFinance) {
                HashMap<String, Finance> finance = (HashMap<String, Finance>) data.getSerializable("finance");
                saveBundle.putSerializable("finance", finance);
            }

            if (isAccessToInformation) {
                HashMap<String, AccessToInformation> accessInfoMap = (HashMap<String, AccessToInformation>) data.getSerializable("accessToInformation");
                saveBundle.putSerializable("accessToInformation", accessInfoMap);
            }

        }
        Intent intent = new Intent(getContext(), UpdateFarmerService.class);
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
        mCurrentPageSequence = mFarmerWizard.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size());
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
        mFarmerWizard.unregisterListener(this);
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
