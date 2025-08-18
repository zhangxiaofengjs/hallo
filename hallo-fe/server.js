const express = require("express");
const cors = require("cors");
const app = express();
const PORT = 3000;

// 使用cors中间件解决跨域问题
app.use(cors());

// 解析JSON请求体
app.use(express.json());

// 模拟联系人数据
const contacts = [
  {
    id: 1,
    name: "张三",
    avatar: "https://picsum.photos/id/1/40/40",
    lastMessage: "你好！",
    lastMessageTime: "10:30",
    unreadCount: 2,
    messages: [
      { id: 1, content: "你好！", senderId: 1, time: "10:28" },
      { id: 2, content: "嗨，最近怎么样？", senderId: 0, time: "10:29" },
      { id: 3, content: "很好，你呢？", senderId: 1, time: "10:30" },
    ],
  },
  {
    id: 2,
    name: "李四",
    avatar: "https://picsum.photos/id/2/40/40",
    lastMessage: "项目进展如何？",
    lastMessageTime: "昨天",
    unreadCount: 0,
    messages: [
      { id: 1, content: "项目进展如何？", senderId: 2, time: "昨天 15:40" },
      {
        id: 2,
        content: "还不错，已经完成了70%",
        senderId: 0,
        time: "昨天 16:20",
      },
    ],
  },
  {
    id: 3,
    name: "王五",
    avatar: "https://picsum.photos/id/3/40/40",
    lastMessage: "周末有安排吗？",
    lastMessageTime: "周一",
    unreadCount: 0,
    messages: [
      { id: 1, content: "周末有安排吗？", senderId: 3, time: "周一 09:15" },
      { id: 2, content: "暂时没有，怎么了？", senderId: 0, time: "周一 09:20" },
      { id: 3, content: "想约你一起看电影", senderId: 3, time: "周一 09:25" },
    ],
  },
];

// 获取联系人列表的API端点
app.get("/api/contacts", (req, res) => {
  res.json(contacts);
});

// 根据ID获取特定联系人的API端点
app.get("/api/contacts/:id", (req, res) => {
  const id = parseInt(req.params.id);
  const contact = contacts.find((c) => c.id === id);

  if (contact) {
    res.json(contact);
  } else {
    res.status(404).json({ message: "联系人未找到" });
  }
});

// 启动服务器
app.listen(PORT, () => {
  console.log(`服务器正在运行，端口：${PORT}`);
});
