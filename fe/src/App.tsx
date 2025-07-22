import { useState, useEffect } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useNavigate,
} from "react-router-dom";
import "./App.less";
import ContactList from "./components/ContactList/index";
import ProfileSection from "./components/ProfileSection/index";
import SearchBar from "./components/SearchBar/index";
import ThemeToggle from "./components/ThemeToggle/index";
import HomePage from "./pages/HomePage";
import ChatPage from "./pages/ChatPage";

// 模拟联系人数据
const mockContacts = [
  {
    id: 1,
    name: "张三",
    avatar: "👨‍💼",
    lastMessage: "你好，最近怎么样？",
    time: "10:30",
    group: "好友",
  },
  {
    id: 2,
    name: "李四",
    avatar: "👩‍💼",
    lastMessage: "明天会议几点开始？",
    time: "昨天",
    group: "同事",
  },
  {
    id: 3,
    name: "王五",
    avatar: "👨‍🚀",
    lastMessage: "项目进展如何？",
    time: "周一",
    group: "同事",
  },
  {
    id: 4,
    name: "赵六",
    avatar: "👩‍🎓",
    lastMessage: "资料我已经发给你了",
    time: "周日",
    group: "好友",
  },
  {
    id: 5,
    name: "钱七",
    avatar: "👨‍🌾",
    lastMessage: "好的，我知道了",
    time: "上周",
    group: "家人",
  },
];

// 创建一个内部组件，可以使用useNavigate钩子
function AppContent() {
  const navigate = useNavigate();
  const [selectedContact, setSelectedContact] = useState(1);
  const [inputValues, setInputValues] = useState<{ [key: number]: string }>({});
  const [filteredContacts, setFilteredContacts] = useState(mockContacts);
  const [searchTerm, setSearchTerm] = useState("");
  // 从localStorage获取主题设置，如果没有则默认为亮色模式
  const [isDarkMode, setIsDarkMode] = useState(() => {
    const savedTheme = localStorage.getItem("theme");
    return savedTheme === "dark";
  });

  // 切换主题并保存到localStorage
  const toggleTheme = () => {
    setIsDarkMode((prevMode) => {
      const newMode = !prevMode;
      localStorage.setItem("theme", newMode ? "dark" : "light");
      return newMode;
    });
  };

  // 监听系统主题变化
  useEffect(() => {
    const mediaQuery = window.matchMedia("(prefers-color-scheme: dark)");

    // 如果用户没有手动设置主题，则使用系统主题
    if (!localStorage.getItem("theme")) {
      setIsDarkMode(mediaQuery.matches);
    }

    // 监听系统主题变化
    const handleChange = (e: MediaQueryListEvent) => {
      if (!localStorage.getItem("theme")) {
        setIsDarkMode(e.matches);
      }
    };

    mediaQuery.addEventListener("change", handleChange);
    return () => mediaQuery.removeEventListener("change", handleChange);
  }, []);

  // 处理搜索
  const handleSearch = (term: string) => {
    setSearchTerm(term);
    if (term.trim() === "") {
      setFilteredContacts(mockContacts);
    } else {
      const filtered = mockContacts.filter(
        (contact) =>
          contact.name.toLowerCase().includes(term.toLowerCase()) ||
          contact.lastMessage.toLowerCase().includes(term.toLowerCase())
      );
      setFilteredContacts(filtered);
    }
  };

  // 切换联系人时更新消息和保存输入内容
  const handleSelectContact = (contactId: number) => {
    // 保存当前联系人的输入内容
    setInputValues((prev) => ({
      ...prev,
      [selectedContact]: prev[selectedContact] || "",
    }));

    setSelectedContact(contactId);
  };

  // 更新输入内容
  const handleInputChange = (value: string) => {
    setInputValues((prev) => ({
      ...prev,
      [selectedContact]: value,
    }));
  };

  // 使用React Router的useNavigate钩子进行无刷新导航
  const handleSelectContactWithRouting = (contactId: number) => {
    handleSelectContact(contactId);
    // 使用navigate进行无刷新导航
    navigate(`/chat/${contactId}`);
  };

  return (
    <div className={`chat-app ${isDarkMode ? "dark-theme" : "light-theme"}`}>
      {/* 左侧区域 */}
      <div className="left-panel">
        <div className="header-section">
          <ProfileSection username="我的账号" status="在线" avatar="👤" />
          <ThemeToggle isDarkMode={isDarkMode} toggleTheme={toggleTheme} />
        </div>
        <SearchBar onSearch={handleSearch} />
        <ContactList
          contacts={filteredContacts}
          selectedContact={selectedContact}
          onSelectContact={handleSelectContactWithRouting}
        />
      </div>
      {/* 右侧区域 - 使用路由 */}
      <Routes>
        <Route path="/" element={<HomePage isDarkMode={isDarkMode} />} />
        <Route
          path="/chat/:contactId"
          element={
            <ChatPage
              inputValues={inputValues}
              onInputChange={handleInputChange}
            />
          }
        />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </div>
  );
}

// 主App组件，只负责提供Router上下文
function App() {
  return (
    <Router>
      <AppContent />
    </Router>
  );
}

export default App;
