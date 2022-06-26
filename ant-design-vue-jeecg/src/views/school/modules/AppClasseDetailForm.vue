<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="学生" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <j-dict-select-tag type="list" v-model="model.name" dictCode="student,name,id" placeholder="请选择学生" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="课程类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classesType">
              <j-dict-select-tag type="list" v-model="model.classesType" dictCode="classes_type" placeholder="请选择课程类型" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="缴费金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="amount">
              <a-input v-model="model.amount" placeholder="请输入缴费金额"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总课时" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalClassHour">
              <a-input-number v-model="model.totalClassHour" placeholder="请输入总课时" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'AppClasseDetailForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
           name: [
              { required: true, message: '请输入学生!'},
           ],
           classesType: [
              { required: true, message: '请输入课程类型!'},
           ],
           amount: [
              { required: true, message: '请输入缴费金额!'},
           ],
           totalClassHour: [
              { required: true, message: '请输入总课时!'},
           ],
        },
        url: {
          add: "/school/appClasseDetail/add",
          edit: "/school/appClasseDetail/edit",
          queryById: "/school/appClasseDetail/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>