import React from "react";
import "../App.less";

interface HomePageProps {
  isDarkMode: boolean;
}

const HomePage: React.FC<HomePageProps> = ({ isDarkMode }) => {
  return (
    <div
      className={`right-panel home-page ${
        isDarkMode ? "dark-theme" : "light-theme"
      }`}
    >
      <div className="welcome-container">
        <div className="welcome-icon">💬</div>
        <h1 className="welcome-title">欢迎使用聊天应用</h1>
        <p className="welcome-message">
          请从左侧选择一个联系人开始聊天，或者搜索联系人。
        </p>
        <div className="welcome-features">
          <div className="feature">
            <span className="feature-icon">🔍</span>
            <span className="feature-text">搜索联系人</span>
          </div>
          <div className="feature">
            <span className="feature-icon">💬</span>
            <span className="feature-text">实时聊天</span>
          </div>
          <div className="feature">
            <span className="feature-icon">🌓</span>
            <span className="feature-text">深色/浅色模式</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
