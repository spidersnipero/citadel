const fs = require("fs");
const csv = require("csv-parser");
const mongoose = require("mongoose");

// === MongoDB Config from Spring Boot application.properties ===
const MONGO_URI = "mongodb://citadel_user:citadel@localhost:27018/inventory?authSource=admin";

// === Inventory Schema ===
// Use `_id` as SKU (string)
const inventorySchema = new mongoose.Schema(
  {
    _id: { type: String, required: true }, // SKU will be stored here
    quantity: { type: Number, min: 0 },
    createdAt: { type: Date, default: Date.now },
    updatedAt: { type: Date, default: Date.now },
  },
  { collection: "inventory" }
);

const InventoryItem = mongoose.model("InventoryItem", inventorySchema);

// === Gaussian Random Generator (0–200 range) ===
function gaussianRandom(mean = 100, stdev = 40) {
  let u = 1 - Math.random();
  let v = 1 - Math.random();
  let z = Math.sqrt(-2.0 * Math.log(u)) * Math.cos(2.0 * Math.PI * v);
  return Math.round(Math.min(Math.max(z * stdev + mean, 0), 200)); // clamp
}

async function insertInventory() {
  await mongoose.connect(MONGO_URI, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  });
  console.log("📦 Connected to MongoDB");

  const items = [];

  fs.createReadStream("skus.csv")
    .pipe(csv())
    .on("data", (row) => {
      if (row.sku) {
        items.push({
          _id: row.sku, // SKU becomes the MongoDB _id
          quantity: gaussianRandom(),
          createdAt: new Date(),
          updatedAt: new Date(),
        });
      }
    })
    .on("end", async () => {
      console.log(`✅ Read ${items.length} SKUs from CSV`);
      try {
        await InventoryItem.insertMany(items, { ordered: false });
        console.log(`✅ Inserted ${items.length} inventory records into MongoDB`);
      } catch (err) {
        console.error("❌ Error inserting inventory:", err);
      } finally {
        await mongoose.disconnect();
        console.log("🔌 Disconnected from MongoDB");
      }
    });
}

insertInventory();
