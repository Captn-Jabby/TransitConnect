import { useState } from "react";
import { styles } from "../styles/styles";

export default function Welcome() {
  const [isOpen, setIsOpen] = useState(true);

  if (!isOpen) {
    return (
      <div
        onClick={() => setIsOpen(true)}
        style={{
          ...styles.card,
          padding: "10px",
          textAlign: "center",
          cursor: "pointer",
          backgroundColor: "#e0f2fe",
          marginBottom: 15,
          color: "#0284c7",
          fontWeight: "bold"
        }}
      >
        👋 New to TransitConnect? Click to read guidelines
      </div>
    );
  }

  return (
    <div style={{ ...styles.card, marginBottom: 20 }}>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: 12
        }}
      >
        <h3 style={{ margin: 0 }}>👋 Welcome to TransitConnect</h3>
        <button
          onClick={() => setIsOpen(false)}
          style={{
            background: "transparent",
            border: "none",
            fontSize: "18px",
            cursor: "pointer",
            color: "#666"
          }}
        >
          ✖
        </button>
      </div>

      <div style={{ maxHeight: "300px", overflowY: "auto", paddingRight: "6px" }}>
        <p>
          <strong>What is this?</strong><br />
          TransitConnect is a community-driven platform for discovering local
          travel routes with clear, practical details.
        </p>

        <p style={{ marginTop: 12 }}>
          <strong>How does it work?</strong><br />
          You don’t just consume data — you help shape it.
        </p>

        <ul style={{ paddingLeft: 20, marginTop: 6 }}>
          <li>Browse routes shared by others</li>
          <li>Add routes based on real travel experience</li>
          <li>Benefit from continuously updated local knowledge</li>
        </ul>

        <h4 style={{ marginTop: 20 }}>🚍 What you can do here</h4>
        <ul style={{ paddingLeft: 20, marginTop: 6 }}>
          <li>Find reliable local routes</li>
          <li>Compare available travel options</li>
          <li>Avoid outdated or confusing information</li>
        </ul>

        <h4 style={{ marginTop: 22, color: "#d97706" }}>
          ⚠️ Community responsibility
        </h4>
        <p style={{ marginTop: 6 }}>
          This platform works only when shared information is accurate.
          Before posting, consider:
        </p>

        <ul style={{ paddingLeft: 20 }}>
          <li>❓Is this route real and currently usable?</li>
          <li>❓Have I personally verified this?</li>
          <li>❓ill this genuinely help someone else?</li>
        </ul>

        <div
          style={{
            background: "#fef2f2",
            padding: 10,
            borderRadius: 6,
            marginTop: 12
          }}
        >
          🚫 <strong>Do not</strong> post false, misleading, duplicate, or irrelevant routes.
          <br />
          ✅ <strong>Only share</strong> accurate, experience-based information.
        </div>

        <p style={{ marginTop: 14 }}>
          Misleading data affects real people’s daily travel decisions.
        </p>

        <h4 style={{ marginTop: 22, color: "#059669" }}>
          🤝 Why this matters
        </h4>
        <p>
          TransitConnect is built by people who travel locally.
          Every accurate contribution improves someone’s journey.
        </p>

        <p style={{ textAlign: "center", fontWeight: "bold", marginTop: 18 }}>
          Ready to explore or contribute responsibly?
        </p>
      </div>
    </div>
  );
}