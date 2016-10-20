package com.example.tak_tix;

import java.util.Locale;
import java.util.Random;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

//����� PlayActivity
public class PlayActivity extends ActionBarActivity {

	// ������ ����
	int Size = 4;

	// ������� ������
	Boolean VictoryCondition = true;

	// ��������� �����:
	// 0-��������
	// 1-�������������
	// 2-�������
	// 3-������ �����
	int[] state;

	// GridLayout ��� ���������� �����
	GridLayout grid;

	// ������ �����
	ImageButton[] ImageButtons;

	// ���������� ��������� �����
	int CountTake;

	Boolean PlayerLastTake;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

		this.setTitle(R.string.app_name);
		loadPreferences();
		Start();
	}

	// �������� ��������
	public void loadPreferences() {

		SharedPreferences preferences = getSharedPreferences("PREF",
				Activity.MODE_PRIVATE);

		VictoryCondition = preferences.getBoolean(getString(R.string.victory),
				true);
		Size = preferences.getInt(getString(R.string.size), 4);

		String lang = preferences.getString(getString(R.string.lang), "en");
		Locale locale = new Locale(lang);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, null);

	}

	// ������ ����
	private void Start() {
		try {
			CountTake = 0;
			// ������ ������� �����
			int params;
			int height = getResources().getDisplayMetrics().heightPixels;
			int width = getResources().getDisplayMetrics().widthPixels;
			if (height > width) {
				params = (width - 80) / Size;
			} else {
				params = (height - 80) / Size;
			}

			// ������� ����
			((TextView) findViewById(R.id.text_move)).setText(R.string.player);
			grid = (GridLayout) findViewById(R.id.grid);
			grid.setColumnCount(Size);

			state = new int[Size * Size];
			ImageButtons = new ImageButton[Size * Size];
			for (int i = 0; i < Size * Size; i++) {
				ImageButtons[i] = new ImageButton(grid.getContext());
				ImageButtons[i]
						.setLayoutParams(new LayoutParams(params, params));
				grid.addView(ImageButtons[i]);
				state[i] = 0;
				ImageButtons[i].setTag("button" + i);
				ImageButtons[i].setOnClickListener(new OnClickImageButton());
			}
			SetBackground();

			Button btn_next_step = (Button) findViewById(R.id.button_next);
			btn_next_step.setOnClickListener(new OnClickOkButton());
		} catch (Exception ex) {
			Dialog("Error", ex.getMessage());
		}
	}

	// ���������� ������� �� �����
	class OnClickImageButton implements View.OnClickListener {
		public void onClick(View v) {
			try {
				int k = Integer.parseInt(v.getTag().toString().substring(6));
				if (state[k] != 1) {
					if (state[k] == 0) {
						state[k] = 2;
						CountTake++;
					} else {
						if (state[k] == 2) {
							state[k] = 0;
							CountTake--;
						}
					}
				}
				SetLocked();
				SetBackground();
			} catch (Exception ex) {
				Dialog("Error", ex.getMessage());
			}
		}
	}

	// ���������� ������� �� ������ OK, ������� ��������� �����
	class OnClickOkButton implements View.OnClickListener {
		public void onClick(View v) {
			int GameOver = 0;
			int SelectedCounter = 0;
			for (int i = 0; i < Size * Size; i++) {
				if (state[i] == 2) {
					SelectedCounter++;
				}
			}
			if (SelectedCounter > 0) {
				for (int i = 0; i < Size * Size; i++) {
					if (state[i] == 2) {
						state[i] = 3;
					}
					if (state[i] == 3) {
						GameOver++;
					}
				}
				CountTake = 0;
				SetLocked();
				SetBackground();
				PlayerLastTake = true;
				((TextView) findViewById(R.id.text_move)).setText(R.string.cpu);
				if (!GameOver(GameOver)) {
					cpuMove();
					((TextView) findViewById(R.id.text_move))
							.setText(R.string.player);
				}
			}
		}
	}

	// ������ ��� ������ � ����������� �� ��������� �����
	private void SetBackground() {

		for (int i = 0; i < Size * Size; i++) {
			if (state[i] == 0) {
				ImageButtons[i].setBackgroundResource(R.drawable.monet);
			} else {
				if (state[i] == 1) {
					ImageButtons[i]
							.setBackgroundResource(R.drawable.monet_locked);
				} else {
					if (state[i] == 2) {
						ImageButtons[i]
								.setBackgroundResource(R.drawable.monet_selected);
					} else {
						if (state[i] == 3) {
							ImageButtons[i]
									.setBackgroundResource(R.drawable.clean);
						}
					}
				}
			}
		}
	}

	// ��������� ����������� �����
	private void SetLocked() {
		// ���� �� ������� �� ����� �����
		if (CountTake == 0) {
			for (int i = 0; i < Size * Size; i++) {
				if (state[i] == 1) {
					state[i] = 0;
				}
			}
		}
		// ���� ������� ���� �����
		if (CountTake == 1) {
			int number = 0;
			while (state[number] != 2 && number < Size * Size - 1) {
				number++;
			}
			int row = number / Size;
			int col = number % Size;
			for (int i = 0; i < Size * Size; i++) {
				if (state[i] == 1) {
					state[i] = 0;
				}
				if (i / Size != row && i % Size != col) {
					if (state[i] == 0) {
						state[i] = 1;
					}
				}
			}

			int k = 0;
			int[] ButtonInRow = new int[Size];
			for (int i = row * Size; i < (row + 1) * Size; i++) {
				ButtonInRow[k++] = i;
			}

			Boolean temp = true;
			for (int i = 0; i < Size; i++) {
				if (state[ButtonInRow[i]] == 3 && ButtonInRow[i] > number) {
					temp = false;
				}
				if (!temp && state[ButtonInRow[i]] == 0) {
					state[ButtonInRow[i]] = 1;
				}
			}

			temp = true;
			for (int i = Size - 1; i >= 0; i--) {
				if (state[ButtonInRow[i]] == 3 && ButtonInRow[i] < number) {
					temp = false;
				}
				if (!temp && state[ButtonInRow[i]] == 0) {
					state[ButtonInRow[i]] = 1;
				}
			}

			k = 0;
			int[] ButtonInCol = new int[Size];
			for (int i = col; i < Size * Size; i = i + Size) {
				ButtonInCol[k++] = i;
			}

			temp = true;
			for (int i = 0; i < Size; i++) {
				if (state[ButtonInCol[i]] == 3 && ButtonInCol[i] > number) {
					temp = false;
				}
				if (!temp && state[ButtonInCol[i]] == 0) {
					state[ButtonInCol[i]] = 1;
				}
			}

			temp = true;
			for (int i = Size - 1; i >= 0; i--) {
				if (state[ButtonInCol[i]] == 3 && ButtonInCol[i] < number) {
					temp = false;
				}
				if (!temp && state[ButtonInCol[i]] == 0) {
					state[ButtonInCol[i]] = 1;
				}
			}
		}
		// ���� ������� ��� �����
		if (CountTake == 2) {
			int number = 0;
			while (state[number] != 2 && number < Size * Size - 1) {
				number++;
			}
			int number2 = number + 1;
			while (state[number2] != 2 && number2 < Size * Size - 1) {
				number2++;
			}
			int row = number / Size;
			int col = number % Size;
			if (row == number2 / Size) {
				for (int i = 0; i < Size * Size; i++) {
					if (i / Size != row && state[i] == 0) {
						state[i] = 1;
					}
				}
			}
			if (col == number2 % Size) {
				for (int i = 0; i < Size * Size; i++) {
					if (i % Size != col && state[i] == 0) {
						state[i] = 1;
					}
				}
			}
		}

	}

	// ��� ����������
	private void cpuMove() {
		Random r = new Random();
		int GameOver = 0;
		// ��� �� �����������
		if (r.nextInt() % 2 == 0) {
			// ����� ������ � �������
			int row = 0;
			int CanTakeCounter = 0;
			do {
				row = r.nextInt(Size);
				for (int i = 0; i < Size; i++) {
					if (state[row * Size + i] == 0) {
						CanTakeCounter++;
					}
				}
			} while (CanTakeCounter == 0);
			// ����� ������ �����
			int numberFirst = -1;
			do {
				numberFirst = r.nextInt(Size);
				if (state[row * Size + numberFirst] == 3) {
					numberFirst = -1;
				}
			} while (numberFirst == -1);

			int LeftCount = 0;
			int RightCount = 0;
			state[row * Size + numberFirst] = 3;
			animation(ImageButtons[row * Size + numberFirst]);
			// ����� ����� ����� � ������ �� ������
			do {
				if ((numberFirst - LeftCount - 1) > 0 && r.nextInt() % 2 == 0) {
					if (state[row * Size + (numberFirst - LeftCount - 1)] == 0) {
						state[row * Size + (numberFirst - LeftCount - 1)] = 3;
						animation(ImageButtons[row * Size
								+ (numberFirst - LeftCount - 1)]);
						LeftCount++;
					}
				}
				if ((numberFirst + RightCount + 1) < Size
						&& r.nextInt() % 2 == 0) {
					if (state[row * Size + (numberFirst + RightCount + 1)] == 0) {
						state[row * Size + (numberFirst + RightCount + 1)] = 3;
						animation(ImageButtons[row * Size
								+ (numberFirst + RightCount + 1)]);
						RightCount++;
					}
				}
			} while (r.nextInt() % 4 == 0);
			// ������� ��������� �����
			for (int i = 0; i < Size * Size; i++) {
				if (state[i] == 2) {
					state[i] = 3;
				}
				if (state[i] == 3) {
					GameOver++;
				}
			}
			// ��� �� ���������
		} else {
			// ����� ������� � �������
			int col = 0;
			int CanTakeCounter = 0;
			do {
				col = r.nextInt(Size);
				for (int i = 0; i < Size; i++) {
					if (state[i * Size + col] == 0) {
						CanTakeCounter++;
					}
				}
			} while (CanTakeCounter == 0);
			// ����� ������ �����
			int numberFirst = -1;
			do {
				numberFirst = r.nextInt(Size);
				if (state[numberFirst * Size + col] == 3) {
					numberFirst = -1;
				}
			} while (numberFirst == -1);
			int UpCount = 0;
			int DownCount = 0;
			state[numberFirst * Size + col] = 3;
			animation(ImageButtons[numberFirst * Size + col]);
			// ����� ����� ������ � ����� �� ������
			do {
				if ((numberFirst - UpCount - 1) > 0 && r.nextInt() % 2 == 0) {
					if (state[(numberFirst - UpCount - 1) * Size + col] == 0) {
						state[(numberFirst - UpCount - 1) * Size + col] = 3;
						animation(ImageButtons[(numberFirst - UpCount - 1)
								* Size + col]);
						UpCount++;
					}

				}
				if ((numberFirst + DownCount + 1) < Size
						&& r.nextInt() % 2 == 0) {
					if (state[(numberFirst + DownCount + 1) * Size + col] == 0) {
						state[(numberFirst + DownCount + 1) * Size + col] = 3;
						animation(ImageButtons[(numberFirst + DownCount + 1)
								* Size + col]);
						DownCount++;
					}
				}
			} while (r.nextInt() % 4 == 0);
			// ������� ��������� �����
			for (int i = 0; i < Size * Size; i++) {
				if (state[i] == 2) {
					state[i] = 3;
				}
				if (state[i] == 3) {
					GameOver++;
				}
			}
		}

		PlayerLastTake = false;
		// �������� �������� �� ����
		GameOver(GameOver);
	}

	// ��������� ��������� ����������� �����
	private void animation(ImageButton img) {
		img.setBackgroundResource(R.drawable.animate);
		AnimationDrawable frameAnimation = (AnimationDrawable) img
				.getBackground();
		frameAnimation.start();
	}

	// �������� ������� �� ����
	private Boolean GameOver(int gameOver) {
		if (gameOver == Size * Size) {
			if (!VictoryCondition) {
				PlayerLastTake = !PlayerLastTake;
			}
			if (PlayerLastTake) {
				((TextView) findViewById(R.id.text_move))
						.setText(R.string.player_win);
				Dialog(getString(R.string.gameover),
						getString(R.string.player_win));
			} else {
				((TextView) findViewById(R.id.text_move))
						.setText(R.string.cpu_win);
				Dialog(getString(R.string.gameover),
						getString(R.string.cpu_win));
			}
			grid.removeAllViewsInLayout();
			Start();
			return true;
		} else {
			return false;
		}
	}

	// ���������� ���� ��� ������ ����������
	private void Dialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(true);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() { // ������ ��
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss(); // ��������� ���������� ����
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	// �������� OptionsMenu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

	// ���������� ������ �������� � OptionsMenu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.set_size:
			showSizeDialog();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// ���������� ���� ������ ������� ����
	public void showSizeDialog() {

		final android.app.Dialog dialog = new android.app.Dialog(
				PlayActivity.this);
		dialog.setTitle(R.string.Boardsize);
		dialog.setCancelable(true);

		LinearLayout linear = new LinearLayout(dialog.getContext());
		linear.setOrientation(LinearLayout.VERTICAL);
		dialog.addContentView(linear, new LayoutParams(getResources()
				.getDisplayMetrics().heightPixels / 2, 600));
		linear.setGravity(Gravity.CENTER);

		final NumberPicker numberpicker = new NumberPicker(linear.getContext());
		numberpicker.setMaxValue(10); //
		numberpicker.setMinValue(3);
		SharedPreferences preferences = getSharedPreferences("PREF",
				Activity.MODE_PRIVATE);
		numberpicker.setValue(preferences.getInt(getString(R.string.size), 4));
		numberpicker.setWrapSelectorWheel(false);
		numberpicker.setGravity(Gravity.CENTER);
		linear.addView(numberpicker, linear.getLayoutParams().width, 350);

		Button btn_okey = new Button(dialog.getContext());
		btn_okey.setText(android.R.string.ok);
		btn_okey.setTextSize(24);
		btn_okey.setGravity(Gravity.CENTER);

		linear.addView(btn_okey, linear.getLayoutParams().width, 120);

		Button btn_cancel = new Button(dialog.getContext());
		btn_cancel.setText(android.R.string.no);
		btn_cancel.setTextSize(24);
		btn_cancel.setGravity(Gravity.CENTER);
		linear.addView(btn_cancel, linear.getLayoutParams().width, 120);

		btn_okey.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Size = numberpicker.getValue();
				grid.removeAllViewsInLayout();
				grid.removeAllViews();
				grid = null;
				Start();
				dialog.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
}
