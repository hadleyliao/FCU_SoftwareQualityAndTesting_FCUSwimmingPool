package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class PricingCalculatorTest {

    private final PricingCalculator calculator = new PricingCalculator();

    // 1. 邊界-獨立非強固
    @Test
    @DisplayName("邊界測試 - 年齡在3歲以下")
    void testAgeBelow3() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculatePrice(false, false, false, 2, false));
        // 測試說明：
        // 此測試檢查當輸入的年齡小於3歲時，系統是否正確拋出IllegalArgumentException。
        // 這是一個邊界測試，因為3歲是有效年齡的下限，測試目的是確保邊界條件被正確處理。
    }

    @Test
    @DisplayName("邊界測試 - 年齡在75歲以上")
    void testAgeAbove75() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculatePrice(false, false, false, 76, false));
        // 測試說明：
        // 此測試檢查當輸入的年齡大於75歲時，系統是否正確拋出IllegalArgumentException。
        // 這是一個邊界測試，因為75歲是有效年齡的上限，測試目的是確保邊界條件被正確處理。
    }

    // 2. 邊界-非獨立非強固
    @Test
    @DisplayName("邊界測試 - 非獨立非強固")
    void testNonIndependentBoundary() {
        assertEquals(160, calculator.calculatePrice(false, false, false, 12, true)); // 年齡折扣 + 早上七點前折扣
        assertEquals(125, calculator.calculatePrice(true, true, false, 30, false)); // 會員折扣 + 週末票價
        // 測試說明：
        // 此測試檢查多個條件的組合情況，例如年齡折扣與早上七點前折扣的疊加效果。
        // 測試目的是確保系統能正確處理非獨立條件的邊界情況。
    }

    // 3. 等價分割測試 - 弱涵蓋
    @Test
    @DisplayName("一般票價測試")
    void testRegularPrice() {
        assertEquals(200, calculator.calculatePrice(false, false, false, 30, false));
        // 測試說明：
        // 此測試檢查在沒有任何折扣條件下，系統是否正確計算一般票價。
        // 測試目的是驗證基本功能是否正常運作。
    }

    @Test
    @DisplayName("會員票價測試")
    void testMemberPrice() {
        assertEquals(100, calculator.calculatePrice(false, true, false, 30, false));
        // 測試說明：
        // 此測試檢查當用戶是會員時，系統是否正確計算會員票價。
        // 測試目的是驗證會員折扣功能是否正確實現。
    }

    @Test
    @DisplayName("團體票價測試")
    void testGroupPrice() {
        assertEquals(140, calculator.calculatePrice(false, false, true, 30, false));
        // 測試說明：
        // 此測試檢查當用戶選擇團體票時，系統是否正確計算團體票價。
        // 測試目的是驗證團體折扣功能是否正確實現。
    }

    // 4. 等價分割測試 - 強涵蓋 (完全組合)
    @Test
    @DisplayName("等價分割測試 - 強涵蓋")
    void testStrongEquivalencePartitioning() {
        assertEquals(100, calculator.calculatePrice(false, true, false, 30, true)); // 會員折扣 + 早上七點前折扣
        assertEquals(140, calculator.calculatePrice(false, false, true, 30, true)); // 團體折扣 + 早上七點前折扣
        assertEquals(125, calculator.calculatePrice(true, true, false, 30, false)); // 會員折扣 + 週末票價
        assertEquals(200, calculator.calculatePrice(false, false, false, 30, false)); // 無折扣
        // 測試說明：
        // 此測試檢查多種條件的完全組合情況，確保所有可能的條件組合都能正確計算票價。
        // 測試目的是驗證系統在複雜條件下的正確性。
    }

    // 5. 等價分割測試 - 強 精實 (例外情況)
    @Test
    @DisplayName("等價分割測試 - 強 精實 (例外情況: 年齡為負數)")
    void testNegativeAgeException() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculatePrice(false, false, false, -1, false));
        // 測試說明：
        // 此測試檢查當輸入的年齡為負數時，系統是否正確拋出IllegalArgumentException。
        // 測試目的是驗證系統能正確處理無效輸入。
    }

    @Test
    @DisplayName("等價分割測試 - 強 精實 (例外情況: 年齡為極大值)")
    void testExtremeAgeException() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculatePrice(false, false, false, 200, false));
        // 測試說明：
        // 此測試檢查當輸入的年齡為極大值時，系統是否正確拋出IllegalArgumentException。
        // 測試目的是驗證系統能正確處理無效輸入。
    }

    // 6. 全成對組合測試
    @Test
    @DisplayName("全成對組合測試")
    void testPairwiseCombinations() {
        assertEquals(100, calculator.calculatePrice(false, true, false, 30, false)); // 會員折扣
        assertEquals(140, calculator.calculatePrice(false, false, true, 30, false)); // 團體折扣
        assertEquals(160, calculator.calculatePrice(false, false, false, 12, false)); // 年齡折扣
        assertEquals(160, calculator.calculatePrice(false, false, false, 30, true)); // 早上七點前折扣
        assertEquals(125, calculator.calculatePrice(true, true, false, 30, false)); // 會員折扣 + 週末票價
        // 測試說明：
        // 此測試檢查多種條件的成對組合情況，確保每對條件的交互作用都能正確計算票價。
        // 測試目的是驗證系統在部分條件組合下的正確性。
    }

    // 7. 決策表測試
    @Test
    @DisplayName("決策表測試")
    void testDecisionTable() {
        assertEquals(100, calculator.calculatePrice(false, true, false, 30, false)); // 會員折扣
        assertEquals(140, calculator.calculatePrice(false, false, true, 30, false)); // 團體折扣
        assertEquals(160, calculator.calculatePrice(false, false, false, 12, false)); // 年齡折扣
        assertEquals(160, calculator.calculatePrice(false, false, false, 30, true)); // 早上七點前折扣
        assertEquals(250, calculator.calculatePrice(true, false, false, 30, false)); // 週末票價
        // 測試說明：
        // 此測試檢查基於決策表的多種情況，確保每個決策條件的組合都能正確計算票價。
        // 測試目的是驗證系統在複雜決策邏輯下的正確性。
    }

    @ParameterizedTest
    @CsvSource({ // 內嵌測試資料在{}裡
        "false, false, false, 30, false, 200", // 一般票價
        "false, true, false, 30, false, 100", // 會員票價
        "false, false, true, 30, false, 140"  // 團體票價
    })
    @DisplayName("使用 CsvSource 測試多組輸入")
    void testWithCsvSource(boolean isWeekend, boolean isMember, boolean isGroup, int age, boolean isEarly, int expectedPrice) {
        assertEquals(expectedPrice, calculator.calculatePrice(isWeekend, isMember, isGroup, age, isEarly));
        // 測試說明：
        // CsvSource 用於內嵌測試資料，適合測試資料量少且固定的情況。
        // 此測試檢查多組輸入是否正確計算票價。
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1) // 引用外部CSV檔案讀取測試資料, 跳過第一行解說訊息
    @DisplayName("使用 CsvFileSource 測試多組輸入")
    void testWithCsvFileSource(boolean isWeekend, boolean isMember, boolean isGroup, int age, boolean isEarly, int expectedPrice) {
        assertEquals(expectedPrice, calculator.calculatePrice(isWeekend, isMember, isGroup, age, isEarly));
        // 測試說明：
        // CsvFileSource 用於從外部 CSV 檔案讀取測試資料，適合測試資料量大或需要頻繁修改的情況。
        // 此測試檢查多組輸入是否正確計算票價。
    }
}
