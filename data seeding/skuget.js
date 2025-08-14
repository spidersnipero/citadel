const fs = require("fs");
const { Client } = require("pg");
const { parse } = require("json2csv");

// === DB CONFIG ===
const client = new Client({
  user: "citadel_user",
  host: "localhost",
  database: "products",
  password: "citadel",
  port: 5434,
});

async function exportSKUs() {
  try {
    await client.connect();
    console.log("📦 Connected to database...");

    // Fetch all SKUs
    const res = await client.query("SELECT sku FROM products ORDER BY sku ASC");

    if (res.rows.length === 0) {
      console.log("⚠ No SKUs found in the database.");
      return;
    }

    // Convert to CSV format
    const csvData = parse(res.rows, { fields: ["sku"] });

    // Save to file
    fs.writeFileSync("skus.csv", csvData, "utf8");
    console.log(`✅ Exported ${res.rows.length} SKUs to skus.csv`);
  } catch (err) {
    console.error("❌ Error exporting SKUs:", err);
  } finally {
    await client.end();
    console.log("🔌 Disconnected from database.");
  }
}

exportSKUs();
