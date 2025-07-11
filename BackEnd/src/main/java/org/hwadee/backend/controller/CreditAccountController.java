package org.hwadee.backend.controller;

import org.hwadee.backend.entity.CreditAccount;
import org.hwadee.backend.service.CreditAccountService;
import org.hwadee.backend.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 学分账户控制器
 */
@RestController
@RequestMapping("/credit/account")
public class CreditAccountController {

    private static final Logger logger = LoggerFactory.getLogger(CreditAccountController.class);

    @Autowired
    private CreditAccountService accountService;

    /**
     * 创建学分账户
     */
    @PostMapping("/create")
    public Result<String> createAccount(@RequestParam Long userId) {
        try {
            logger.info("创建学分账户，用户ID: {}", userId);
            return accountService.createAccount(userId);
        } catch (Exception e) {
            logger.error("创建账户时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 根据用户ID获取学分账户
     */
    @GetMapping("/user/{userId}")
    public Result<CreditAccount> getAccountByUserId(@PathVariable Long userId) {
        try {
            return accountService.getAccountByUserId(userId);
        } catch (Exception e) {
            logger.error("查询账户时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 更新学分账户
     */
    @PutMapping("/update")
    public Result<String> updateAccount(@RequestBody CreditAccount account) {
        try {
            return accountService.updateAccount(account);
        } catch (Exception e) {
            logger.error("更新账户时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 增加学分
     */
    @PostMapping("/add")
    public Result<String> addCredits(@RequestParam Long userId, @RequestParam Double credits) {
        try {
            logger.info("增加学分，用户ID: {}, 学分: {}", userId, credits);
            return accountService.addCredits(userId, credits);
        } catch (Exception e) {
            logger.error("增加学分时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 消费学分
     */
    @PostMapping("/deduct")
    public Result<String> deductCredits(@RequestParam Long userId, @RequestParam Double credits) {
        try {
            logger.info("消费学分，用户ID: {}, 学分: {}", userId, credits);
            return accountService.deductCredits(userId, credits);
        } catch (Exception e) {
            logger.error("消费学分时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 冻结学分
     */
    @PostMapping("/freeze")
    public Result<String> freezeCredits(@RequestParam Long userId, @RequestParam Double credits) {
        try {
            logger.info("冻结学分，用户ID: {}, 学分: {}", userId, credits);
            return accountService.freezeCredits(userId, credits);
        } catch (Exception e) {
            logger.error("冻结学分时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }

    /**
     * 解冻学分
     */
    @PostMapping("/unfreeze")
    public Result<String> unfreezeCredits(@RequestParam Long userId, @RequestParam Double credits) {
        try {
            logger.info("解冻学分，用户ID: {}, 学分: {}", userId, credits);
            return accountService.unfreezeCredits(userId, credits);
        } catch (Exception e) {
            logger.error("解冻学分时发生异常", e);
            return Result.error("服务器内部错误");
        }
    }
} 