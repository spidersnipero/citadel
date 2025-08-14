const fs = require("fs");
const csv = require("csv-parser");
const { Client } = require("pg");
const { faker } = require("@faker-js/faker");
const { v4: uuidv4 } = require("uuid");

// === DB CONFIG ===
const client = new Client({
  user: "citadel_user",
  host: "localhost",
  database: "products",
  password: "citadel",
  port: 5434,
});

const IMAGE_URLS = [
  "https://citadel-mumbai-bucket.s3.ap-south-1.amazonaws.com/955a35d9-7b2c-42ed-ab9f-65df1974a91e-1.jpeg",
  "https://citadel-mumbai-bucket.s3.ap-south-1.amazonaws.com/ee8bf654-65ed-49b1-98b6-7f9a929c814f-1.jpeg"
];

const CREATOR_EMAILS = ["ajseller@g.c", "ajadmin@g.c"];

// Generate a random uppercase string (length between 4 and 6)
function randomLetters(lenMin = 3, lenMax = 4) {
  const length = faker.number.int({ min: lenMin, max: lenMax });
  return Array.from({ length }, () =>
    String.fromCharCode(65 + Math.floor(Math.random() * 26))
  ).join("");
}


function generateSKU(colorname, fit) {


  const randomNum = faker.number.int({ min: 1000, max: 9999 });

  // Combine
  let rawSKU = `${randomLetters()}-${randomLetters()}-${randomNum}`;


  return rawSKU;
}


// Parse stringified array into a single string
function parseArrayField(value) {
  if (!value) return "";
  try {
    const parsed = JSON.parse(value);
    if (Array.isArray(parsed)) {
      return parsed.join(" "); // join with space (or "\n" for multiline)
    }
  } catch (err) {
    // Not JSON, return as is
    return value;
  }
  return value;
}

// Store classifications to avoid duplicate category inserts
const classificationMap = {};
const productsData = [];
const usedSKUs = new Set();

async function insertData() {
  await client.connect();

  // Insert categories
  const categoriesInsert = [];
  for (const classification in classificationMap) {
    categoriesInsert.push([classificationMap[classification], classification, new Date()]);
  }

  if (categoriesInsert.length > 0) {
    await client.query(
      `INSERT INTO categories (id, name, created_at) VALUES ${categoriesInsert
        .map((_, i) => `($${i * 3 + 1}, $${i * 3 + 2}, $${i * 3 + 3})`)
        .join(", ")}`,
      categoriesInsert.flat()
    );
  }
  console.log(`✅ Inserted ${categoriesInsert.length} categories`);

  // Insert products
  const productValues = [];
  for (const p of productsData) {
    productValues.push([
      p.id, p.name, p.description, p.sku, p.price, p.categoryId,
      p.creatorEmail, p.isActive, p.createdAt, p.updatedAt, p.imageUrls
    ]);
  }

  if (productValues.length > 0) {
    await client.query(
      `INSERT INTO products (id, name, description, sku, price, category_id, creator_email, is_active, created_at, updated_at, image_url)
       VALUES ${productValues
         .map((_, i) => `($${i * 11 + 1}, $${i * 11 + 2}, $${i * 11 + 3}, $${i * 11 + 4}, $${i * 11 + 5}, $${i * 11 + 6}, $${i * 11 + 7}, $${i * 11 + 8}, $${i * 11 + 9}, $${i * 11 + 10}, $${i * 11 + 11})`)
         .join(", ")}`,
      productValues.flat()
    );
  }
  console.log(`✅ Inserted ${productValues.length} products`);

  await client.end();
}

// === Read CSV ===
fs.createReadStream("ecp.csv")
  .pipe(csv())
  .on("data", (row) => {
    if (row.name && row.fulldescription && row.Classification) {
      const classification = row.Classification.trim();

      // Create category ID if not already stored
      if (!classificationMap[classification]) {
        classificationMap[classification] = uuidv4();
      }

      // Ensure unique SKU
      let sku;
      do {
        sku = generateSKU(row.colorname, row.fit);
      } while (usedSKUs.has(sku));
      usedSKUs.add(sku);

      // Parse description from fulldescription
      const parsedDescription = parseArrayField(row.fulldescription);

      // Push product data
      productsData.push({
        id: uuidv4(),
        name: row.name,
        description: parsedDescription,
        sku,
        price: faker.number.float({ min: 700, max: 2000, precision: 0.01 }),
        categoryId: classificationMap[classification],
        creatorEmail: CREATOR_EMAILS[Math.floor(Math.random() * CREATOR_EMAILS.length)],
        isActive: true,
        createdAt: new Date(),
        updatedAt: new Date(),
        imageUrls: IMAGE_URLS
      });
    }
  })
  .on("end", async () => {
    console.log("CSV file processed. Inserting into DB...");
    await insertData();
    console.log("✅ All data inserted successfully.");
  });
