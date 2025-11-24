# 測試指南

## 測試參數說明
在測試中，以下參數將被頻繁使用，請參考此表以了解每個參數的意義：

| 參數名稱       | 類型    | 說明                                   |
|--------------|-------|--------------------------------------|
| isWeekend    | boolean | 是否為週末，`true` 表示週末，`false` 表示非週末 |
| isMember     | boolean | 是否為會員，`true` 表示是會員，`false` 表示非會員 |
| isGroup      | boolean | 是否為團體，`true` 表示是團體，`false` 表示非團體 |
| age          | int    | 年齡，表示顧客的年齡                         |
| isBefore7AM  | boolean | 是否為早上七點前，`true` 表示是，`false` 表示否     |

---

## 簡介
本指南詳細說明了專案中所有測試的內容、測試目的以及相關名詞的解釋，並附上程式碼範例與圖示，幫助您理解測試的設計邏輯。

---

## 測試內容與目的

### 1. 邊界測試 - 獨立非強固
**測試內容**：
- 測試年齡小於 3 歲的情況。
- 測試年齡大於 75 歲的情況。

**測試目的**：
- 確保系統能正確處理無效的邊界輸入，並拋出 `IllegalArgumentException`。

**程式碼範例**：
```java
@Test
@DisplayName("邊界測試 - 年齡在3歲以下")
void testAgeBelow3() {
    assertThrows(IllegalArgumentException.class, () -> calculator.calculatePrice(false, false, false, 2, false));
}

@Test
@DisplayName("邊界測試 - 年齡在75歲以上")
void testAgeAbove75() {
    assertThrows(IllegalArgumentException.class, () -> calculator.calculatePrice(false, false, false, 76, false));
}
```

---

### 2. 邊界測試 - 非獨立非強固
**測試內容**：
- 測試多個條件的組合，例如：
  - 年齡折扣與早上七點前折扣的疊加。
  - 會員折扣與週末票價的疊加。

**測試目的**：
- 確保系統能正確處理多個條件的邊界情況。

**程式碼範例**：
```java
@Test
@DisplayName("邊界測試 - 非獨立非強固")
void testNonIndependentBoundary() {
    assertEquals(160, calculator.calculatePrice(false, false, false, 12, true));
    assertEquals(125, calculator.calculatePrice(true, true, false, 30, false));
}
```

---

### 3. 等價分割測試 - 弱涵蓋
**測試內容**：
- 測試以下基本情況：
  - 一般票價。
  - 會員票價。
  - 團體票價。

**測試目的**：
- 驗證基本功能是否正常運作。

**程式碼範例**：
```java
@Test
@DisplayName("一般票價測試")
void testRegularPrice() {
    assertEquals(200, calculator.calculatePrice(false, false, false, 30, false));
}

@Test
@DisplayName("會員票價測試")
void testMemberPrice() {
    assertEquals(100, calculator.calculatePrice(false, true, false, 30, false));
}

@Test
@DisplayName("團體票價測試")
void testGroupPrice() {
    assertEquals(140, calculator.calculatePrice(false, false, true, 30, false));
}
```

---

### 4. 等價分割測試 - 強涵蓋（完全組合）
**測試內容**：
- 測試多種條件的完全組合，例如：
  - 會員折扣 + 早上七點前折扣。
  - 團體折扣 + 早上七點前折扣。
  - 會員折扣 + 週末票價。
  - 無折扣。

**測試目的**：
- 確保所有可能的條件組合都能正確計算票價。

**程式碼範例**：
```java
@Test
@DisplayName("等價分割測試 - 強涵蓋")
void testStrongEquivalencePartitioning() {
    assertEquals(100, calculator.calculatePrice(false, true, false, 30, true));
    assertEquals(140, calculator.calculatePrice(false, false, true, 30, true));
    assertEquals(125, calculator.calculatePrice(true, true, false, 30, false));
    assertEquals(200, calculator.calculatePrice(false, false, false, 30, false));
}
```

---

### 5. 等價分割測試 - 強精實（例外情況）
**測試內容**：
- 測試例外情況：
  - 年齡為負數。
  - 年齡為極大值（如 200）。

**測試目的**：
- 確保系統能正確處理無效輸入，並拋出 `IllegalArgumentException`。

**程式碼範例**：
```java
@Test
@DisplayName("等價分割測試 - 強 精實 (例外情況: 年齡為負數)")
void testNegativeAgeException() {
    assertThrows(IllegalArgumentException.class, () -> calculator.calculatePrice(false, false, false, -1, false));
}

@Test
@DisplayName("等價分割測試 - 強 精實 (例外情況: 年齡為極大值)")
void testExtremeAgeException() {
    assertThrows(IllegalArgumentException.class, () -> calculator.calculatePrice(false, false, false, 200, false));
}
```

---

### 6. 全成對組合測試
**測試內容**：
- 測試多種條件的成對組合，例如：
  - 會員折扣。
  - 團體折扣。
  - 年齡折扣。
  - 早上七點前折扣。
  - 會員折扣 + 週末票價。

**測試目的**：
- 確保每對條件的交互作用都能正確計算票價。

**程式碼範例**：
```java
@Test
@DisplayName("全成對組合測試")
void testPairwiseCombinations() {
    assertEquals(100, calculator.calculatePrice(false, true, false, 30, false));
    assertEquals(140, calculator.calculatePrice(false, false, true, 30, false));
    assertEquals(160, calculator.calculatePrice(false, false, false, 12, false));
    assertEquals(160, calculator.calculatePrice(false, false, false, 30, true));
    assertEquals(125, calculator.calculatePrice(true, true, false, 30, false));
}
```

---

### 7. 決策表測試
**測試內容**：
- 基於決策表測試多種情況，例如：
  - 會員折扣。
  - 團體折扣。
  - 年齡折扣。
  - 早上七點前折扣。
  - 週末票價。

**測試目的**：
- 確保每個決策條件的組合都能正確計算票價。

**程式碼範例**：
```java
@Test
@DisplayName("決策表測試")
void testDecisionTable() {
    assertEquals(100, calculator.calculatePrice(false, true, false, 30, false));
    assertEquals(140, calculator.calculatePrice(false, false, true, 30, false));
    assertEquals(160, calculator.calculatePrice(false, false, false, 12, false));
    assertEquals(160, calculator.calculatePrice(false, false, false, 30, true));
    assertEquals(250, calculator.calculatePrice(true, false, false, 30, false));
}
```

---

## 名詞解釋與圖示

### 名詞解釋
- **邊界測試**：檢查輸入值是否在有效範圍內，並正確處理邊界情況。
- **等價分割測試**：將輸入分為多個等價類別，測試每個類別的代表值。
- **全成對組合測試**：測試多個條件的成對組合，確保交互作用正確。
- **決策表測試**：基於決策表，測試所有可能的條件組合。

### 圖示
以下是測試邏輯的簡單流程圖：

```plaintext
+-------------------+
|   輸入條件檢查    |
+-------------------+
         |
         v
+-------------------+
|   計算票價邏輯    |
+-------------------+
         |
         v
+-------------------+
|   返回計算結果    |
+-------------------+
```

---

## 測試工具與方法

### @CsvSource
- **用途**：
  - 用於內嵌測試資料，適合測試資料量少且固定的情況。
- **範例**：
  ```java
  @ParameterizedTest
  @CsvSource({
      "false, false, false, 30, false, 200",
      "false, true, false, 30, false, 100"
  })
  void testWithCsvSource(boolean isWeekend, boolean isMember, boolean isGroup, int age, boolean isEarly, int expectedPrice) {
      // 測試邏輯
  }
  ```

### @CsvFileSource
- **用途**：
  - 用於從外部 CSV 檔案讀取測試資料，適合測試資料量大或需要頻繁修改的情況。
- **範例**：
  ```java
  @ParameterizedTest
  @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
  void testWithCsvFileSource(boolean isWeekend, boolean isMember, boolean isGroup, int age, boolean isEarly, int expectedPrice) {
      // 測試邏輯
  }
  ```

---

## 結論
本專案的測試涵蓋了所有需求，並處理了所有可能的例外情況。測試資料完整，測試方法清晰，能有效驗證系統的正確性與穩定性。
