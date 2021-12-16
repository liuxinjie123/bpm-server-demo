package com.flowable.bpm.controller;

import com.flowable.bpm.common.Result;
import org.flowable.engine.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "expense")
public class ExpenseController {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    /**
     * 添加报销
     *
     * @param userId    用户Id
     * @param money     报销金额
     * @param descption 描述
     */
    @PostMapping(value = "add")
    public Result addExpense(String userId, Integer money, String descption) {
        //启动流程
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskUser", userId);
        map.put("money", money);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Expense", map);
        return Result.success("提交成功.流程Id为：" + processInstance.getId());
    }

    /**
     * 获取审批管理列表
     */
    @GetMapping(value = "/list")
    public Result list(String userId) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
        for (Task task : tasks) {
            System.out.println(task.toString());
        }
        return Result.setData(tasks);
    }

    /**
     * 批准
     *
     * @param taskId 任务ID
     */
    @PostMapping(value = "apply")
    public Result apply(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return Result.error("流程不存在");
        }
        //通过审核
        HashMap<String, Object> map = new HashMap<>();
        map.put("outcome", "通过");
        taskService.complete(taskId, map);
        return Result.success("processed ok!");
    }

    /**
     * 拒绝
     */
    @PostMapping(value = "reject")
    public Result reject(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return Result.error("流程不存在");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("outcome", "驳回");
        taskService.complete(taskId, map);
        return Result.success("reject");
    }


}
