package agh.cs.gameOfLife.interfaces;

import agh.cs.gameOfLife.GameOfLife;

public interface IUserInterfaceContract {
    interface EventListener {
        void onKeyPressed();
        void onDialogClick();
    }

    interface View {
        void updateGame(GameOfLife game);
        void updateGraph(GameOfLife game);
        void setListener(IUserInterfaceContract.EventListener listener);
        void showDialog(String message);
        void showError(String message);
    }


}
