package rec.games.pokemon.teambuilder.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rec.games.pokemon.teambuilder.R;
import rec.games.pokemon.teambuilder.db.SavedTeam;
import rec.games.pokemon.teambuilder.db.SavedTeamRepository;
import rec.games.pokemon.teambuilder.model.Team;

public class TeamListFragment extends Fragment implements OnTeamClickListener
{
	private static final String TAG = TeamFragment.class.getSimpleName();

	private LiveData<List<SavedTeam>> mLiveTeamList;

	private TeamListAdapter mTeamListAdapter;
	private RecyclerView teamRV;
	private SavedTeamRepository mSavedTeamRepo;

	private TextView mLoadingErrorMsgTV;
	private LinearLayout mLoadingErrorLL;
	private Button mLoadingErrorBtn;

	private FloatingActionButton mAddTeamButton;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.team_list, container, false);

		mTeamListAdapter = new TeamListAdapter(this);

		teamRV = view.findViewById(R.id.rv_team_list);
		teamRV.setLayoutManager(new LinearLayoutManager(getContext()));
		teamRV.setItemAnimator(new DefaultItemAnimator());
		teamRV.setNestedScrollingEnabled(false);
		teamRV.setAdapter(mTeamListAdapter);

		//error button
		mLoadingErrorMsgTV = view.findViewById(R.id.tv_loading_error);
		mLoadingErrorLL = view.findViewById(R.id.ll_loading_error);
		mLoadingErrorBtn = view.findViewById(R.id.btn_loading_error);

		mAddTeamButton = view.findViewById(R.id.fab_add_team);
		mAddTeamButton.hide();

		mSavedTeamRepo = new SavedTeamRepository(getActivity().getApplication());
		mLiveTeamList = mSavedTeamRepo.getAllTeams();

		mLiveTeamList.observe(this, new Observer<List<SavedTeam>>()
		{
			@Override
			public void onChanged(@Nullable List<SavedTeam> savedTeamList)
			{
				if(savedTeamList == null)
				{
					teamRV.setVisibility(View.GONE);

					mLoadingErrorMsgTV.setVisibility(View.VISIBLE);
					mLoadingErrorLL.setVisibility(LinearLayout.VISIBLE);
					mLoadingErrorBtn.setVisibility(View.VISIBLE);

					mAddTeamButton.hide();
				}
				else
				{
					teamRV.setVisibility(View.VISIBLE);

					mLoadingErrorMsgTV.setVisibility(View.INVISIBLE);
					mLoadingErrorLL.setVisibility(LinearLayout.INVISIBLE);
					mLoadingErrorBtn.setVisibility(View.INVISIBLE);

					mAddTeamButton.show();

					mTeamListAdapter.setTeamList(savedTeamList);

				}
			}
		});

		mAddTeamButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO: create a new team...
			}
		});

		teamRV.addOnScrollListener(new RecyclerView.OnScrollListener()
		{
			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
			{
				if(dy > 0 || dy < 0 && mAddTeamButton.isShown())
					mAddTeamButton.hide();                            //hide if scrolling
			}

			@Override
			public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
			{
				if(newState == RecyclerView.SCROLL_STATE_IDLE)
					mAddTeamButton.show();
				super.onScrollStateChanged(recyclerView, newState);
			}
		});

		return view;
	}

	public void onTeamClicked(SavedTeam savedTeam)
	{
		Intent intent = new Intent(getContext(), TeamDetailActivity.class);
		intent.putExtra(Team.TEAM_ID, savedTeam.id);
		startActivity(intent);
	}
}
