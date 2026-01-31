import { styles } from "../styles/styles";

export default function BottomNav({ onHome, onAdd, onTop }) {
  return (
    <div style={styles.bottomNav}>
      <div style={styles.navBar}>
        <button style={styles.navBtn} onClick={onHome}>Home</button>
        <button style={styles.navBtn} onClick={onAdd}>Add Route</button>
        <button style={styles.navBtn} onClick={onTop}>Top</button>
      </div>
    </div>
  );
}
